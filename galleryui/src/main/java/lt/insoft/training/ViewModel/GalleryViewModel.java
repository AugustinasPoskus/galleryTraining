package lt.insoft.training.ViewModel;

import lt.insoft.training.model.Folder;
import lt.insoft.training.services.IFolderService;
import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GalleryViewModel {

    private List<Folder> folderList;
    //private ListModelList<Folder> folderList;
    private byte[] image = this.getFileBytes();
    private String name = "test";

    @WireVariable
    private IFolderService folderService;

    public GalleryViewModel() {
    }

    @Init
    public void init() {
        folderList = new ArrayList<Folder>();

        //if(fl.isEmpty()){
            //folderList = new ArrayList<Folder>();
        //}
        //else{
        //    folderList = fl;
        //}
        //folderList = folderService.getAllFolders();
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Command
    public void add(){
        Folder folder = new Folder();
        folder.setName("Unnamed");
        folderService.addFolder(folder);
        //folderList.add(folder);
    }

    public List<Folder> getFolderList(){
        return folderList;
    }


    private byte[] getFileBytes() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("folder.png");
            byte[] bytes = IOUtils.toByteArray(is);
            return bytes;
        } catch (IOException e) {
            System.out.println(e);
        }
        return new byte[0];

    }
}