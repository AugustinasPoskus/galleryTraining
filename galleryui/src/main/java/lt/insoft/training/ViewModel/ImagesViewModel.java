package lt.insoft.training.ViewModel;

import Validators.LengthValidator;
import Validators.QualityValidator;
import Validators.TagsValidator;
import Validators.UploadValidator;
import lt.insoft.training.ViewModel.Utils.ImageScale;
import lt.insoft.training.model.*;
import lt.insoft.training.services.FolderService;
import lt.insoft.training.services.PictureService;
import org.apache.commons.io.IOUtils;
import org.springframework.orm.jpa.JpaSystemException;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import java.io.IOException;
import java.io.InputStream;
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
    LengthValidator pictureNameValidator = new LengthValidator();
    QualityValidator qualityValidator = new QualityValidator();
    TagsValidator tagsValidator = new TagsValidator();
    UploadValidator uploadValidator = new UploadValidator();
    private final int PAGINATION_BY = 8;
    private final String MESSAGE = "Picture was already modified, please reload page and repeat your action!";

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
                pictureThumbnailList = pictureService.getPictureThumbnail(0, PAGINATION_BY, folder.getId());
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

    private byte[] getFileBytes() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("folder.png");
            byte[] bytes = IOUtils.toByteArray(is);
            return bytes;
        } catch (IOException e) {
            Clients.evalJavaScript("modalWarning('" + e.getLocalizedMessage() + "');");
        }
        return new byte[0];
    }

    @Command
    @NotifyChange({"selectedPicture", "tags"})
    public void setImageInformation(@BindingParam("id") Long id) {
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
            Clients.evalJavaScript("modalWarning('" + MESSAGE + "');");
        }
    }

    @Command
    @NotifyChange("selectedPicture")
    public void open(@BindingParam("id") Long id) {
        selectedPicture = pictureService.getPictureInfoById(id);
        Clients.evalJavaScript("openFullPicture();");
    }

    @Command
    @NotifyChange("fileName")
    public void doUploadFile(@BindingParam("file") Media image) {
        if (!image.equals(null)) {
            if (!image.getContentType().startsWith("image/")) {
                String message = "The request was rejected because the file (" + image.getName() + ") is not an image!";
                Clients.evalJavaScript("modalWarning( '" + message + "' );");
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
        Clients.evalJavaScript("dismissFormModal();");
        if (pictureData.getData() != null && thumbnail.getData() != null) {
            List<String> tagList = Arrays.asList(tags.split(","));
            pictureService.addPicture(picture, pictureData, thumbnail, folder, tagList);
            if (pictureThumbnailList.size() < PAGINATION_BY) {
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
    @NotifyChange({"pictureThumbnailList", "picturesCount"})
    public void remove(@BindingParam("id") Long id) {
        try {
            pictureService.removePictureByThumbnail(id);
            pictureThumbnailList.removeIf(p -> p.getId().equals(id));
            picturesCount--;
            if (picturesCount > currentPage * PAGINATION_BY + pictureThumbnailList.size()) {
                pictureThumbnailList.add(pictureService.getPictureThumbnail(PAGINATION_BY - 1, 1, folder.getId()).get(0));
            }
        } catch (NoResultException e) {
            Clients.evalJavaScript("modalWarning('" + MESSAGE + "');");
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
    @NotifyChange({"tags"})
    public void update() {
        List<String> tagList = Arrays.asList(tags.split(","));
        tags = "";
        try {
            pictureService.updatePicture(selectedPicture, selectedThumbnailId, tagList);
        } catch (OptimisticLockException optLocke) {
            Clients.evalJavaScript("modalWarning('" + MESSAGE + "');");
        } catch(JpaSystemException jpaE){
            Clients.evalJavaScript("modalWarning('" + MESSAGE + "');");
        }
        Clients.evalJavaScript("dismissEdit();");
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
    public void setSelectedThumbnailId(@BindingParam("id") Long selectedThumbnailId) {
        this.selectedThumbnailId = selectedThumbnailId;
        try {
            this.selectedPicture = pictureService.getPictureInfoById(selectedThumbnailId);
            Clients.evalJavaScript("confirmationModal();");
        } catch (NoResultException e) {
            Clients.evalJavaScript("modalWarning('" + MESSAGE + "');");
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
        pictureThumbnailList = pictureService.getPictureThumbnail(currentPage * PAGINATION_BY, PAGINATION_BY, folder.getId());
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

    public int getPAGINATION_BY() {
        return PAGINATION_BY;
    }
}
