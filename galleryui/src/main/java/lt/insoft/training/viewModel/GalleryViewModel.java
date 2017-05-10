package lt.insoft.training.viewModel;

import lt.insoft.training.model.Folder;
import lt.insoft.training.services.FolderService;
import lt.insoft.training.validators.LengthValidator;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import java.util.List;

public class GalleryViewModel {
    @WireVariable
    private FolderService folderService;
    private Folder folder = new Folder();
    private String folderName;
    private Long selectedId;
    private int foldersCount = 0;
    private int currentPage = 0;
    private final int PAGE_SIZE = 6;
    private ListModelList<Folder> availableFolders;
    private String errorMessage = "";
    private LengthValidator folderNameValidator = new LengthValidator();
    private boolean isWarning = false;

    @Init
    public void init() {
        foldersCount = folderService.foldersCount();
        int firstFolderIndex = currentPage * PAGE_SIZE;
        availableFolders = new ListModelList(folderService.getFolders(firstFolderIndex, PAGE_SIZE));
    }

    @Command
    public void open(@BindingParam("id") Long id) {
        Executions.sendRedirect(UriComponentsBuilder.fromUriString("images.zul").queryParam("folderId", id).build().encode().toString());
    }

    @Command
    @NotifyChange({"availableFolders", "foldersCount", "folder"})
    public void add() {
        folderService.addFolder(folder);
        if (availableFolders.size() < PAGE_SIZE) {
            availableFolders.add(folder);
        }
        foldersCount++;
        folder = new Folder();
    }

    @Command
    @NotifyChange({"availableFolders", "foldersCount", "errorMessage", "warning"})
    public void remove(@BindingParam("id") Long id) {
        isWarning = false;
        if (this.containsId(id)) {
            try {
                if (folderService.removeFolder(id)) {
                    availableFolders.removeIf(p -> p.getId().equals(id));
                    foldersCount--;
                    if (foldersCount > currentPage * PAGE_SIZE + availableFolders.size()) {
                        availableFolders.add(folderService.getFolders(PAGE_SIZE - 1, 1).get(0));
                    } else if(foldersCount != 0 && availableFolders.size() == 0 && currentPage > 0){
                        currentPage--;
                        this.paging();
                    }
                }
            } catch (JpaSystemException jpaE) {
                this.callModalWarning("Folder was already deleted! Please reload page and repeat your operation!");
                isWarning = true;
            }
        }
    }

    @Command
    @NotifyChange({"availableFolders", "folder", "errorMessage", "warning"})
    public void editFolderName() {
        isWarning = false;
        String oldName = "";
        this.folderName = folder.getName();
        folder = new Folder();
        if (this.containsId(this.getSelectedId()) && !(this.folderName.equals(""))) {
            List<Folder> list = this.getAvailableFolders();
            for (Folder folder : list) {
                if (folder.getId().equals(this.selectedId)) {
                    try {
                        oldName = folder.getName();
                        folder = folderService.updateFolder(folder, this.folderName);
                        this.mergeFolders(folder);
                        break;
                    } catch (OptimisticLockException optLocke) {
                        folder.setName(oldName);
                        this.callModalWarning("Folder name was already changed! Please reload page and repeat your operation!");
                        isWarning = true;
                        break;
                    } catch (EntityNotFoundException entitynotFound){
                        folder.setName(oldName);
                        this.callModalWarning("Folder was already changed! Please reload page and repeat your operation!");
                        isWarning = true;
                        break;
                    }
                }
            }
        }
    }

    private void mergeFolders(Folder folder) {
        for (int i = 0; i < availableFolders.size(); i++) {
            if (availableFolders.get(i).getId().equals(folder.getId())) {
                availableFolders.set(i, folder);
                break;
            }
        }
    }

    public boolean containsId(Long id) {
        List<Folder> list = this.getAvailableFolders();
        for (Folder object : list) {
            if (object.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    @Command
    @NotifyChange("folderName")
    public void setSelectedId(@BindingParam("id") Long id) {
        this.selectedId = id;
        for (Folder folder : availableFolders) {
            if (folder.getId().equals(this.selectedId)) {
                this.folderName = folder.getName();
            }
        }
    }

    @Command
    @NotifyChange({"folder","selectedId"})
    public void undo() {
        this.selectedId = null;
        folder = new Folder();
    }

    public int getFoldersCount() {
        return foldersCount;
    }

    public void setFoldersCount(int foldersCount) {
        this.foldersCount = foldersCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Command
    @NotifyChange({"foldersCount", "availableFolders"})
    public void paging() {
        foldersCount = folderService.foldersCount();
        availableFolders = new ListModelList(folderService.getFolders(currentPage * PAGE_SIZE, PAGE_SIZE));
    }

    public ListModelList<Folder> getAvailableFolders() {
        return availableFolders;
    }

    public void setAvailableFolders(ListModelList<Folder> availableFolders) {
        this.availableFolders = availableFolders;
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    public LengthValidator getFolderNameValidator() {
        return folderNameValidator;
    }

    public void setFolderNameValidator(LengthValidator folderNameValidator) {
        this.folderNameValidator = folderNameValidator;
    }

    @Command
    public void prepareFolder() {
    }

    @Command
    @NotifyChange({"selectedId"})
    public void showWarningOnFolderRemove(@BindingParam("id") Long id) {
        this.selectedId = id;
    }

    @Command
    @NotifyChange({"selectedId", "folderName"})
    public void prepareEditFolderName(@BindingParam("id") Long id){
        this.selectedId = id;
        for (Folder folder : availableFolders) {
            if (folder.getId().equals(this.selectedId)) {
                this.folderName = folder.getName();
            }
        }
    }

    @Command
    @NotifyChange("errorMessage")
    public void callModalWarning(String warning){
        this.errorMessage = warning;
    }

    @Command
    public void close(){

    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }
}
