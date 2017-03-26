package lt.insoft.training.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AU_PICTURE")
public class Picture {

    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE_INSERT")
    private Date date;

    @Column(name = "QUALITY")
    private String quality;

    @Column(name = "PICTURE_NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String desciption;

    @Column(name = "DATA_ID")
    private Long dataId;

    @Column(name = "FOLDER_ID")
    private Long folderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Long getPictureId() {
        return dataId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public void setPictureId(Long pictureId) {
        this.dataId = pictureId;
    }

    public Picture() {}

}
