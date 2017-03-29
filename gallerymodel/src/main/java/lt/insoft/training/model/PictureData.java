package lt.insoft.training.model;


import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "AU_PICTURE_DATA")
public class PictureData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA")
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public PictureData() {}

}
