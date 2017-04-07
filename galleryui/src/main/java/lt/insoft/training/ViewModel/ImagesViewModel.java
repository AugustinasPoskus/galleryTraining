package lt.insoft.training.ViewModel;

import lt.insoft.training.ViewModel.Utils.ImageScale;
import lt.insoft.training.model.Folder;
import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureData;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.services.FolderService;
import lt.insoft.training.services.PictureService;
import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImagesViewModel {

    @WireVariable
    private PictureService pictureService;
    @WireVariable
    private FolderService folderService;
    private Picture picture = new Picture();
    private PictureData pictureData = new PictureData();
    private Thumbnail thumbnail = new Thumbnail();
    private String fileName = "No picture uploaded!";
    private List<Thumbnail> pictureThumbnailList;
    private Long folderId;
    private int paginationBy = 12;

    @Init
    public void init(@ContextParam(ContextType.EXECUTION) Execution execution) {
        String parameter = execution.getParameter("folderId");
        try{
            folderId = Long.parseLong(parameter);
            System.out.println(folderId);
            pictureThumbnailList =  pictureService.getPictureThumbnail(0,paginationBy, folderId);
            System.out.println(pictureThumbnailList.size());

        }catch (NumberFormatException ex) {
            System.out.println("page not found");
        }
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Long getValue() {
        return folderId;
    }

    public void setValue(Long value) {
        this.folderId = value;
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

    @Command
    public void open(@BindingParam("id") Long id){
        Picture pictureInfo = pictureService.getPictureInfoById(id);
        System.out.println(pictureInfo.getId() + " " + pictureInfo.getName() + " " + pictureInfo.getDesciption());
    }

    @Command
    @NotifyChange("fileName")
    public void doUploadFile(@BindingParam("file") Media image) {
        if(!image.equals(null)) {
            if (!image.getContentType().startsWith("image/")) {
                Messagebox.show("Not an image: " + image.getName(), "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            this.fileName = "Attached: " + image.getName();
            pictureData.setData(image.getByteData());
            String type = image.getFormat();
            thumbnail.setData(ImageScale.resizeImage(pictureData.getData(), type));
        }
    }

    @Command
    @NotifyChange({"pictureThumbnailList", "fileName"})
    public void add(){
        if(pictureData.getData() != null && thumbnail.getData() != null ){
            System.out.println(picture.getName() + " " + picture.getDesciption() + " " + picture.getQuality());
            picture.setPictureData(pictureData);
            Folder folder = folderService.getFolder(folderId);
            pictureService.addPicture(picture, pictureData, thumbnail, folder);
            pictureThumbnailList.add(thumbnail);
        }
        picture = new Picture();
        pictureData = new PictureData();
        this.fileName= "No picture uploaded!";
    }

    @Command
    @NotifyChange("fileName")
    public void undo(){
        picture = new Picture();
        pictureData = new PictureData();
        this.fileName= "No picture uploaded!";
    }

    public List<Thumbnail> getPictureThumbnailList() {
        return pictureThumbnailList;
    }

    public void setPictureThumbnailList(List<Thumbnail> pictureThumbnailList) {
        this.pictureThumbnailList = pictureThumbnailList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
