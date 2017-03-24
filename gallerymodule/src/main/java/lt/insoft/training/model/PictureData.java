package lt.insoft.training.model;

import javax.persistence.*;

@Entity
@Table(name = "AU_PICTURE_DATA")
public class PictureData {

    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "IMAGE")
    private byte[] image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public PictureData() {}

}
