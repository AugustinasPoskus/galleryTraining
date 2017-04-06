package lt.insoft.training.model;

import javax.persistence.*;

@Entity
@Table(name = "AU_THUMBNAIL")
public class Thumbnail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "THUMBNAIL")
    private byte[] thumbnailData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return thumbnailData;
    }

    public void setData(byte[] data) {
        this.thumbnailData = data;
    }
}
