package lt.insoft.training.ViewModel;


import lt.insoft.training.ViewModel.Utils.ImageScale;
import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureData;
import lt.insoft.training.services.PictureDataService;
import lt.insoft.training.services.PictureService;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.util.Date;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImagesViewModel {

    @WireVariable
    private PictureDataService pictureDataService;
    private Picture picture = new Picture();
    private byte[] photo;
    private byte[] photoThumbnail;
    @WireVariable
    private PictureService pictureService;
    private List<PictureData> pictureThumbnailList;
    private byte[] image = this.getFileBytes();
    private Long folderId;

    @Init
    public void init() {
        pictureThumbnailList = pictureDataService.getPictureThumbnail(0,10);
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
        //Picture picture = pictureService.getPictureByDataId(id);
        //System.out.println(id+ " " + picture.getName() + " " + picture.getDesciption());
    }

    @Command
    public void doUploadFile(@BindingParam("file") Media image) {
        if(!image.equals(null)) {
            if (!image.getContentType().startsWith("image/")) {
                Messagebox.show("Not an image: " + photo, "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            this.photo = image.getByteData();
            BufferedImage thumbnail = null;
            try {
                thumbnail = ImageScale.resizeImage(this.photo, 256,256);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println("Mime Type of " + image.getName() + " is " + image.getFormat());
            try {
                ImageIO.write( thumbnail, image.getFormat(), baos );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                baos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.photoThumbnail = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Command
    @NotifyChange("pictureThumbnailList")
    public void add(){
        System.out.println(picture.getName() + " " + picture.getDesciption() + " " + picture.getQuality());
        picture.setDate(new Date());
        PictureData pictureData = new PictureData();
        pictureData.setData(this.photo);
        pictureData.setThumbnail(this.photoThumbnail);
        picture.setPictureData(pictureData);
        pictureDataService.addPictureData(pictureData);
        pictureService.addPicture(picture);
        pictureThumbnailList.add(pictureData);
    }

    public List<PictureData> getPictureThumbnailList() {
        return pictureThumbnailList;
    }

    public void setPictureThumbnailList(List<PictureData> pictureThumbnailList) {
        this.pictureThumbnailList = pictureThumbnailList;
    }
}
