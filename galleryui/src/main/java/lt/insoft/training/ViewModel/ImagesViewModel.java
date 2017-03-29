package lt.insoft.training.ViewModel;


import lt.insoft.training.model.PictureData;
import lt.insoft.training.services.PictureDataService;
import lt.insoft.training.services.PictureService;
import org.apache.commons.io.IOUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ImagesViewModel {

    @WireVariable
    private PictureDataService pictureDataService;
    private byte[] photo;
    @WireVariable
    private PictureService pictureService;
    private List<PictureData> pictureDataList;
    private byte[] image = this.getFileBytes();
    private Long value;

    @Init
    public void init() {
        Session session = Sessions.getCurrent();
        if (session.hasAttribute("id")) {
            value = (Long) session.getAttribute("id");
        }
        pictureDataList = pictureDataService.getPictureData(0,10);
    }

    public Long getValue() {
        return value;
    }

    public List<PictureData> getPictureDataList() {
        return pictureDataList;
    }

    public void setValue(Long value) {
        this.value = value;
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
    public void doUploadFile(@BindingParam("file") Media photo) throws SQLException {
        if (!photo.getContentType().startsWith("image/")) {
            Messagebox.show("Not an image: "+photo, "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        this.photo = photo.getByteData();
    }

    @Command
    @NotifyChange("pictureDataList")
    public void add(){
        PictureData pictureData = new PictureData();
        pictureData.setData(photo);
        pictureDataService.addPictureData(pictureData);
        pictureDataList.add(pictureData);
    }
}
