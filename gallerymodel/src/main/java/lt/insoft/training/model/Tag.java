package lt.insoft.training.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AU_TAG")
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    private Set<Picture> pictures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "AU_PICTURE_TAG", joinColumns = { @JoinColumn(name = "PICTURE_ID") }, inverseJoinColumns = { @JoinColumn(name = "TAG_ID") })
    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }
}
