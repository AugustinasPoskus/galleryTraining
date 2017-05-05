package lt.insoft.training.viewModel;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lt.insoft.training.model.*;
import lt.insoft.training.services.FolderService;
import lt.insoft.training.services.PictureService;
import lt.insoft.training.validators.LengthValidator;
import lt.insoft.training.validators.QualityValidator;
import lt.insoft.training.validators.TagsValidator;
import lt.insoft.training.validators.UploadValidator;
import lt.insoft.training.viewModel.utils.ImageScale;
import org.springframework.orm.jpa.JpaSystemException;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    private int picturesCount = 0;
    private int currentPage = 0;
    private LengthValidator pictureNameValidator = new LengthValidator();
    private QualityValidator qualityValidator = new QualityValidator();
    private TagsValidator tagsValidator = new TagsValidator();
    private UploadValidator uploadValidator = new UploadValidator();
    private final int PAGE_SIZE = 8;
    private final String MESSAGE = "Picture was already modified, please reload page and repeat your action!";
    private String errorMessage = "";
    private boolean isWarning = false;

    @Init
    public void init(@ContextParam(ContextType.EXECUTION) Execution execution) {
        String parameter = execution.getParameter("folderId");
        try {
            Long folderId = Long.parseLong(parameter);
            folder = folderService.getFolder(folderId);
            if (folder == null) {
                Executions.sendRedirect("pageNotFound.zul");
            } else {
                picturesCount = pictureService.getPicturesCount(folder.getId());
                pictureThumbnailList = pictureService.getPictureThumbnail(0, PAGE_SIZE, folder.getId());
            }
        } catch (NumberFormatException ex) {
            Executions.sendRedirect("pageNotFound.zul");
        }
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

//    private byte[] getFileBytes() {
//        try {
//            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//            InputStream is = classloader.getResourceAsStream("folder.png");
//            byte[] bytes = IOUtils.toByteArray(is);
//            return bytes;
//        } catch (IOException e) {
//
//            Clients.evalJavaScript("modalWarning('" + e.getLocalizedMessage() + "');");
//        }
//        return new byte[0];
//    }

    @Command
    @NotifyChange({"selectedPicture", "tags"})
    public void setImageInformation(@BindingParam("id") Long id) {
        isWarning = false;
        try {
            selectedThumbnailId = id;
            selectedPicture = pictureService.getPictureInfoById(id);
            tags = "";
            List<Tag> list = selectedPicture.getTags();
            for (int i = 0; i < list.size(); i++) {
                tags += list.get(i).getName();
                if (i + 1 != list.size()) {
                    tags += ", ";
                }
            }
        } catch (NoResultException e) {
            selectedPicture = null;
        }
    }

    @Command
    @NotifyChange("selectedPicture")
    public void open(@BindingParam("id") Long id) {
        try {
            selectedPicture = pictureService.getPictureInfoById(id);
        } catch (NoResultException e){
            selectedPicture = null;
        }
    }

    @Command
    @NotifyChange({"fileName", "warning", "errorMessage"})
    public void doUploadFile(@BindingParam("file") Media image) {
        isWarning = false;
        if (!image.equals(null)) {
            if (!image.getContentType().startsWith("image/")) {
                errorMessage = "The request was rejected because the file (" + image.getName() + ") is not an image!";
                isWarning = true;
                return;
            }
            this.fileName = "Attached: " + image.getName();
            pictureData.setData(image.getByteData());
            String type = image.getFormat();
            thumbnail.setData(ImageScale.resizeImage(pictureData.getData(), type));
        }
    }

    @Command
    @NotifyChange({"pictureThumbnailList", "fileName", "picture", "tags", "picturesCount"})
    public void add() {
        if (pictureData.getData() != null && thumbnail.getData() != null) {
            List<String> tagList = Arrays.asList(tags.split(","));
            pictureService.addPicture(picture, pictureData, thumbnail, folder, tagList);
            if (pictureThumbnailList.size() < PAGE_SIZE) {
                pictureThumbnailList.add(thumbnail);
            }
            picturesCount++;
        }
        picture = new Picture();
        pictureData = new PictureData();
        thumbnail = new Thumbnail();
        tags = "";
        this.fileName = "No picture uploaded!";
    }

    @Command
    @NotifyChange({"pictureThumbnailList", "picturesCount", "errorMessage", "warning"})
    public void remove(@BindingParam("id") Long id) {
        isWarning = false;
        try {
            pictureService.removePictureByThumbnail(id);
            pictureThumbnailList.removeIf(p -> p.getId().equals(id));
            picturesCount--;
            if (picturesCount > currentPage * PAGE_SIZE + pictureThumbnailList.size()) {
                pictureThumbnailList.add(pictureService.getPictureThumbnail(PAGE_SIZE - 1, 1, folder.getId()).get(0));
            } else if(picturesCount != 0 && pictureThumbnailList.size() == 0 && currentPage > 0){
                currentPage--;
                this.paging();
            }
        } catch (NoResultException e) {
            errorMessage = MESSAGE;
            isWarning = true;
        }
    }

    @Command
    @NotifyChange({"fileName", "picture", "tags"})
    public void undo() {
        picture = new Picture();
        pictureData = new PictureData();
        thumbnail = new Thumbnail();
        tags = "";
        this.fileName = "No picture uploaded!";
    }

    @Command
    @NotifyChange({"tags", "errorMessage", "warning"})
    public void update() {
        List<String> tagList = Arrays.asList(tags.split(","));
        tags = "";
        try {
            pictureService.updatePicture(selectedPicture, selectedThumbnailId, tagList);
        } catch (OptimisticLockException optLocke) {
            errorMessage = MESSAGE;
            isWarning = true;
        } catch(JpaSystemException jpaE){
            errorMessage = MESSAGE;
            isWarning = true;
        }
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
    @NotifyChange({"selectedPicture"})
    public void setSelectedThumbnailId(@BindingParam("id") Long selectedThumbnailId) {
        this.selectedThumbnailId = selectedThumbnailId;
        try {
            this.selectedPicture = pictureService.getPictureInfoById(selectedThumbnailId);
            //Clients.evalJavaScript("confirmationModal();");
        } catch (NoResultException e) {
            selectedPicture = null;
        }
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

    public int getPicturesCount() {
        return picturesCount;
    }

    public void setPicturesCount(int picturesCount) {
        this.picturesCount = picturesCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Command
    @NotifyChange({"picturesCount", "pictureThumbnailList"})
    public void paging() {
        picturesCount = pictureService.getPicturesCount(folder.getId());
        pictureThumbnailList = pictureService.getPictureThumbnail(currentPage * PAGE_SIZE, PAGE_SIZE, folder.getId());
    }

    public LengthValidator getPictureNameValidator() {
        return pictureNameValidator;
    }

    public QualityValidator getQualityValidator() {
        return qualityValidator;
    }

    public TagsValidator getTagsValidator() {
        return tagsValidator;
    }

    public UploadValidator getUploadValidator() {
        return uploadValidator;
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    @Command
    @NotifyChange({"selectedPicture", "tags"})
    public void setImageInformationBeforeEdit(@BindingParam("id") Long id) {
        try {
            selectedThumbnailId = id;
            selectedPicture = pictureService.getPictureInfoById(id);
            tags = "";
            List<Tag> list = selectedPicture.getTags();
            for (int i = 0; i < list.size(); i++) {
                tags += list.get(i).getName();
                if (i + 1 != list.size()) {
                    tags += ", ";
                }
            }
        } catch (NoResultException e) {
            selectedPicture = null;
        }
    }

    @Command
    @NotifyChange({"fileName", "picture", "tags"})
    public void prepareUpload(){
        picture = new Picture();
        pictureData = new PictureData();
        thumbnail = new Thumbnail();
        tags = "";
        this.fileName = "No picture uploaded!";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    @Command
    public void close(){

    }

}
