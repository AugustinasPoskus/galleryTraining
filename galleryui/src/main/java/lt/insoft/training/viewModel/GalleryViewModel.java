package lt.insoft.training.viewModel;

import lt.insoft.training.validators.LengthValidator;
import lt.insoft.training.model.Folder;
import lt.insoft.training.services.FolderService;
import lt.insoft.training.viewModel.utils.ImageToZkImageConverter;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.List;

public class GalleryViewModel {
    @WireVariable
    private FolderService folderService;
    private Folder folder = new Folder();
    private String folderName;
    private Long selectedId;
    private int foldersCount = 0;
    private int currentPage = 0;
    private final int PAGINATION_BY = 6;
    ListModelList<Folder> availableFolders;
    LengthValidator folderNameValidator = new LengthValidator();

    @Init
    public void init() {
        foldersCount = folderService.foldersCount();
        int firstFolderIndex = currentPage * PAGINATION_BY;
        availableFolders = new ListModelList(folderService.getFolders(firstFolderIndex, PAGINATION_BY));
    }

    @Command
    public void open(@BindingParam("id") Long id) {
        Executions.sendRedirect(UriComponentsBuilder.fromUriString("images.zul").queryParam("folderId", id).build().encode().toString());
    }

    @Command
    @NotifyChange({"availableFolders", "foldersCount", "folder"})
    public void add() {
        Clients.evalJavaScript("dismissAddFolderModal();");
        folderService.addFolder(folder);
        if (availableFolders.size() < PAGINATION_BY) {
            availableFolders.add(folder);
        }
        foldersCount++;
        folder = new Folder();
    }

    @Command
    @NotifyChange({"availableFolders", "foldersCount"})
    public void remove(@BindingParam("id") Long id) {
        if (this.containsId(id)) {
            try {
                if (folderService.removeFolder(id)) {
                    availableFolders.removeIf(p -> p.getId().equals(id));
                    foldersCount--;
                    if (foldersCount > currentPage * PAGINATION_BY + availableFolders.size()) {
                        availableFolders.add(folderService.getFolders(PAGINATION_BY - 1, 1).get(0));
                    }
                }
            } catch (JpaSystemException jpaE) {
                String message = "Folder was already deleted! Please reload page and repeat your operation!";
                Clients.evalJavaScript("modalWarning('" + message + "');");
            }
        }
    }

    @Command
    @NotifyChange({"availableFolders", "folder"})
    public void editFolderName() {
        Clients.evalJavaScript("dismissChangeNameModal();");
        String oldName = "";
        this.folderName = folder.getName();
        folder = new Folder();
        if (this.containsId(this.selectedId) && !(this.folderName.equals(""))) {
            List<Folder> list = this.getAvailableFolders();
            for (Folder folder : list) {
                if (folder.getId().equals(this.selectedId)) {
                    try {
                        oldName = folder.getName();
                        folder = folderService.updateFolder(folder, this.folderName);
                        this.mergeFolders(folder);
                        break;
                    } catch (JpaSystemException optLocke) {
                        folder.setName(oldName);
                        String message = "Folder name was already changed! Please reload page and repeat your operation!";
                        Clients.evalJavaScript("modalWarning('" + message + "');");
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
    @NotifyChange({"folder"})
    public void undo() {
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
        availableFolders = new ListModelList(folderService.getFolders(currentPage * PAGINATION_BY, PAGINATION_BY));
    }

    public ListModelList<Folder> getAvailableFolders() {
        return availableFolders;
    }

    public void setAvailableFolders(ListModelList<Folder> availableFolders) {
        this.availableFolders = availableFolders;
    }

    public int getPAGINATION_BY() {
        return PAGINATION_BY;
    }

    public LengthValidator getFolderNameValidator() {
        return folderNameValidator;
    }

    public void setFolderNameValidator(LengthValidator folderNameValidator) {
        this.folderNameValidator = folderNameValidator;
    }
}
