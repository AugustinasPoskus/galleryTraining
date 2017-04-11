package lt.insoft.training.ViewModel;

import lt.insoft.training.model.Folder;
import lt.insoft.training.services.FolderService;
import org.hibernate.TransactionException;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import javax.persistence.PersistenceException;
import java.util.List;

public class GalleryViewModel {
    @WireVariable
    private FolderService folderService;
    private Folder folder = new Folder();
    private String folderName;
    private Long selectedId;
    private int foldersCount = 0;
    private int currentPage = 0;
    private final int paginationBy = 6;
    ListModelList<Folder> availableFolders;

    @Init
    public void init() {
        //Executions.sendRedirect("pageNotFound.zul");
        foldersCount = folderService.foldersCount();
        int firstFolderIndex = currentPage * paginationBy;
        availableFolders = new ListModelList(folderService.getFolders(firstFolderIndex, paginationBy, currentPage));
    }

    @Command
    public void open(@BindingParam("id") Long id) {
        Executions.sendRedirect(UriComponentsBuilder.fromUriString("images.zul").queryParam("folderId", id).build().encode().toString());
    }

    @Command
    @NotifyChange({"availableFolders", "foldersCount", "folder"})
    public void add() {
        folderService.addFolder(folder);
        if (availableFolders.size() < paginationBy) {
            availableFolders.add(folder);
        }
        foldersCount++;
        folder = new Folder();
    }

    @Command
    @NotifyChange({"availableFolders" , "foldersCount"})
    public void remove(@BindingParam("id") Long id) {
        if (this.containsId(id)) {
            try{
                if (folderService.removeFolder(id)) {
                    availableFolders.removeIf(p -> p.getId().equals(id));
                    foldersCount--;
                    if (foldersCount > currentPage * paginationBy + availableFolders.size()) {
                        availableFolders.add(folderService.getFolders(paginationBy - 1, 1, currentPage).get(0));
                    }
                }
            } catch (JpaSystemException jpaE){
                String message = "Folder was already deleted! Please reload page and repeat your operation!";
                Clients.evalJavaScript("failedToChangeFolder('" + message + "');");
            }
        }
    }

    @Command
    @NotifyChange("availableFolders")
    public void editFolderName() {
        String oldName = "";
        if (this.containsId(this.selectedId) && !(this.folderName.equals(""))) {
            List<Folder> list = this.getAvailableFolders();
                for (Folder folder : list) {
                    if (folder.getId().equals(this.selectedId)) {
                        try{
                            oldName = folder.getName();
                            folder = folderService.updateFolder(folder , this.folderName);
                            this.mergeFolders(folder);
                            break;
                        } catch(JpaSystemException optLocke){
                            folder.setName(oldName);
                            String message = "Folder name was already changed! Please reload page and repeat your operation!";
                            Clients.evalJavaScript("failedToChangeFolder('" + message + "');");
                            break;
                        }
                    }

                }
        }
    }

    private void mergeFolders(Folder folder){
        for (int i=0;i<availableFolders.size();i++) {
            if(availableFolders.get(i).getId().equals(folder.getId())){
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
    public void undo(){
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
    @NotifyChange({"foldersCount","availableFolders"})
    public void paging(){
        foldersCount = folderService.foldersCount();
        availableFolders = new ListModelList(folderService.getFolders(currentPage * paginationBy, paginationBy, currentPage));
    }

    public ListModelList<Folder> getAvailableFolders() {
        return availableFolders;
    }

    public void setAvailableFolders(ListModelList<Folder> availableFolders) {
        this.availableFolders = availableFolders;
    }

    public int getPaginationBy() {
        return paginationBy;
    }

    public Validator getRangeValidator() {
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                Number maxLength = 15;
                if (ctx.getProperty().getValue() instanceof String) {
                    String value = (String) ctx.getProperty().getValue();
                    if (value.length() > maxLength.longValue()) {
                        this.addInvalidMessage(ctx, "string", "Your passwords do not match!");
                    }
                } else {
                    this.addInvalidMessage(ctx, "string", "Your passwords do not match!");
                }
            }
        };
    }
}
