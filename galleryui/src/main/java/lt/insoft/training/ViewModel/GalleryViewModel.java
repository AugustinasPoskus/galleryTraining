package lt.insoft.training.ViewModel;

import lt.insoft.training.model.Folder;
import lt.insoft.training.services.FolderService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GalleryViewModel {
    private List<Folder> folderList;

    @WireVariable
    private FolderService folderService;

    @Init
    public void init() {
        folderList = folderService.getAllFolders();
    }

    @Command
    public void open(@BindingParam("id") Long id){
        Session session = Sessions.getCurrent();
        session.setAttribute("id", id);
        Executions.sendRedirect("images.zul");
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
    public void editFolderName(@BindingParam("id") Long id){
        if(this.containsId(id)){
            if(folderService.updateFolder(id, "naujas")){
                for(Folder folder : folderList) {
                    if(folder.getId().equals(id)) {
                        folder.setName("naujas");
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

}