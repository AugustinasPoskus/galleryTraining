package lt.insoft.training.ViewModel;

import lt.insoft.training.model.Folder;
import lt.insoft.training.services.FolderService;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

public class GalleryViewModel {
    private List<Folder> folderList;
    private String folderName;
    private Long selectedId;

    @WireVariable
    private FolderService folderService;

    @Init
    public void init() {
        folderList = folderService.getAllFolders();
    }

    @Command
    public void open(@BindingParam("id") Long id){
        Executions.sendRedirect(UriComponentsBuilder.fromUriString("images.zul").queryParam("folderId", id).build().encode().toString());
    }

    @Command
    @NotifyChange("folderList")
    public void add(){
        Folder folder = new Folder();
        folder.setName("Unnamed");
        folderService.addFolder(folder);
        folderList.add(folder);
    }

    @Command
    @NotifyChange("folderList")
    public void remove(@BindingParam("id") Long id){
        if(this.containsId(id)){
            if(folderService.removeFolder(id)){
                folderList.removeIf(p -> p.getId().equals(id));
            }
        }
    }

    @Command
    @NotifyChange("folderList")
    public void editFolderName(){
        if (this.containsId(this.selectedId) && !(this.folderName.equals(""))) {
            if (folderService.updateFolder(this.selectedId, this.folderName)) {
                for (Folder folder : folderList) {
                    if (folder.getId().equals(this.selectedId)) {
                        folder.setName(this.folderName);
                    }
                }
            }
        }
    }

    public List<Folder> getFolderList(){
        return folderList;
    }

    public boolean containsId( Long id) {
        List<Folder> list = this.getFolderList();
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
        for (Folder folder : folderList) {
            if (folder.getId().equals(this.selectedId)) {
                this.folderName = folder.getName();
            }
        }
    }
}