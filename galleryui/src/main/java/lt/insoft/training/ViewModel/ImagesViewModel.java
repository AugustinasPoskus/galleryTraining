package lt.insoft.training.ViewModel;

import lt.insoft.training.ViewModel.Utils.ImageScale;
import lt.insoft.training.model.*;
import lt.insoft.training.services.FolderService;
import lt.insoft.training.services.PictureService;
import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImagesViewModel {

    @WireVariable
    private PictureService pictureService;
    @WireVariable
    private FolderService folderService;
    private Picture picture = new Picture();
    private Picture selectedPicture;
    private Folder folder;
    private String tags = "";
    private PictureData pictureData = new PictureData();
    private Long selectedThumbnailId;
    private Thumbnail thumbnail = new Thumbnail();
    private String fileName = "No picture uploaded!";
    private List<Thumbnail> pictureThumbnailList;
    private int paginationBy = 8;

    @Init
    public void init(@ContextParam(ContextType.EXECUTION) Execution execution) {
        System.out.println(new java.util.Date());
        String parameter = execution.getParameter("folderId");
        try{
            Long folderId = Long.parseLong(parameter);
            folder = folderService.getFolder(folderId);
            if(folder == null){

            } else{
                pictureThumbnailList =  pictureService.getPictureThumbnail(0,paginationBy, folder.getId());
            }
        }catch (NumberFormatException ex) {

        }
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
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
    @NotifyChange("selectedPicture")
    public void setImageInformation(@BindingParam("id") Long id){
        selectedPicture = pictureService.getPictureInfoById(id);
    }

    @Command
    public void open(@BindingParam("id") Long id){
        Picture pictureInfo = pictureService.getPictureInfoById(id);
        System.out.println(pictureInfo.getId() + " " + pictureInfo.getName() + " " + pictureInfo.getDescription());
    }

    @Command
    @NotifyChange("fileName")
    public void doUploadFile(@BindingParam("file") Media image) {
        if(!image.equals(null)) {
            if (!image.getContentType().startsWith("image/")) {
                //Messagebox.show("Not an image: " + image.getName(), "Error", Messagebox.OK, Messagebox.ERROR);
                String message = "The request was rejected because the file (" + image.getName() + ") is not an image!";
                Clients.evalJavaScript("failedUpload( '" + message + "' );");
                return;
            }
            this.fileName = "Attached: " + image.getName();
            pictureData.setData(image.getByteData());
            String type = image.getFormat();
            thumbnail.setData(ImageScale.resizeImage(pictureData.getData(), type));
        }
    }

    @Command
    @NotifyChange({"pictureThumbnailList", "fileName", "picture", "tags"})
    public void add(){
        Clients.evalJavaScript("dismissFormModal();");
        if(pictureData.getData() != null && thumbnail.getData() != null ){
            System.out.println(picture.getName() + " " + picture.getDescription() + " " + picture.getQuality());
            List<String> tagList = Arrays.asList(tags.split(","));
            pictureService.addPicture(picture, pictureData, thumbnail, folder, tagList);
            pictureThumbnailList.add(thumbnail);
        }
        picture = new Picture();
        pictureData = new PictureData();
        thumbnail = new Thumbnail();
        tags = "";
        this.fileName= "No picture uploaded!";
    }

    @Command
    @NotifyChange({"pictureThumbnailList"})
    public void remove(@BindingParam("id") Long id){
        pictureService.removePictureByThumbnail(id);
        pictureThumbnailList.removeIf(p -> p.getId().equals(id));
    }

    @Command
    @NotifyChange({"fileName", "picture"})
    public void undo(){
        picture = new Picture();
        pictureData = new PictureData();
        thumbnail = new Thumbnail();
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

    public Picture getSelectedPicture() {
        return selectedPicture;
    }

    public void setSelectedPicture(Picture selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    public void setSelectedPicture(Long id) {
        this.selectedPicture = selectedPicture;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Long getSelectedThumbnailId() {
        return selectedThumbnailId;
    }

    @Command
    public void setSelectedThumbnailId(@BindingParam("id") Long selectedThumbnailId) {
        this.selectedThumbnailId = selectedThumbnailId;
        System.out.println(selectedThumbnailId);
        this.selectedPicture = pictureService.getPictureInfoById(selectedThumbnailId);
    }

    public PictureData getPictureData() {
        return pictureData;
    }

    public void setPictureData(PictureData pictureData) {
        this.pictureData = pictureData;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
