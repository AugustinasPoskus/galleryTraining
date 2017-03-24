package lt.insoft.training.ViewModel;


import lt.insoft.training.insoft.training.model.Folder;
import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;



import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GalleryViewModel {

    private ArrayList<Folder> folderList = new ArrayList<Folder>();
    byte[] image = this.getFileBytes();
    int i = 0;
    String name = "test";

    public byte[] getImage() {
        return image;
    }

    @Command
    @NotifyChange("image")
    public void setImage(byte[] image) {
        this.image = image;
    }

    @Command
    @NotifyChange("folderList")
    public void add(){
        Folder folder = new Folder();
        folder.setName("Unnamed");
        folderList.add(folder);
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Folder> getFolderList(){
        return folderList;
    }

    @Command
    @NotifyChange("name")
    public void changeName(){
        this.name = "change " + i ;
        i++;
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