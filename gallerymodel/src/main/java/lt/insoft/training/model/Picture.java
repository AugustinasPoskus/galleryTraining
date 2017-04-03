package lt.insoft.training.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AU_PICTURE")
public class Picture {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE_INSERT")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "QUALITY")
    private String quality;

    @Column(name = "PICTURE_NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String desciption;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DATA_ID")
    private PictureData pictureData;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FOLER_ID", nullable = false)
//    private Folder folder;

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

    public Picture() {}

    public PictureData getPictureData() {
        return pictureData;
    }

    public void setPictureData(PictureData pictureData) {
        this.pictureData = pictureData;
    }

}
