package lt.insoft.training.ViewModel;

import lt.insoft.training.model.Folder;
import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import java.io.IOException;
import java.io.InputStream;


public class GalleryViewModel {

    private ListModelList<Folder> folderList = new ListModelList<Folder>();
    byte[] image = this.getFileBytes();
    String name = "test";

    @Init
    public void init() {
        Folder folder = new Folder();
        folder.setName("Unnamed1");
        folderList.add(folder);
        folder = new Folder();
        folder.setName("Unnamed2");
        folderList.add(folder);
        folder = new Folder();
        folder.setName("Unnamed3");
        folderList.add(folder);
    }

    public byte[] getImage() {
        return image;
    }

    @Command
    @NotifyChange("image")
    public void setImage(byte[] image) {
        this.image = image;
    }

    @Command
    public void add(){
        Folder folder = new Folder();
        folder.setName("Unnamed");
        folderList.add(folder);
    }

    public ListModelList<Folder> getFolderList(){
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