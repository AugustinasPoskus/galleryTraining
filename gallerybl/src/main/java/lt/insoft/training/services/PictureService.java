package lt.insoft.training.services;

import lt.insoft.training.Repositories.PictureDataRepository;
import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.Repositories.ThumbnailRepository;
import lt.insoft.training.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

@Service("pictureService")
public class PictureService {
    @Autowired
    private PictureRepository pictureRep;
    @Autowired
    private PictureDataRepository pictureDataRep;
    @Autowired
    private ThumbnailRepository thumbnailRep;
    @Autowired
    private TagService tagService;
    private List<Picture> pictures;

    public PictureService() {
    }

    @Transactional
    public long addPicture(Picture picture, PictureData pictureData, Thumbnail thumbnail, Folder folder, List<String> tagList) {
        List<Tag> tags = new ArrayList<Tag>();
        for (String val : tagList) {
            val = val.trim();
        }
        tagList = new ArrayList<String>(new LinkedHashSet<String>(tagList));
        for (String val : tagList) {
            if (!val.isEmpty()) {
                Tag tag = tagService.addTag(val);
                tags.add(tag);
            }
        }
        picture.setTags(tags);
        picture.setPictureData(pictureData);
        picture.setThumbnail(thumbnail);
        picture.setFolder(folder);
        Date date = new Date();
        picture.setDate(date);
        pictures.add(picture);
        return pictureRep.insertPicture(picture);
    }

    @Transactional
    public boolean removePictureByThumbnail(Long id) {
        try {
            Picture picture = this.getPictureInfoById(id);
            picture.setTags(null);
            if (pictureRep.removePicture(picture.getId())) {
                pictures.remove(picture);
                return true;
            }
        } catch (NoResultException e) {
            throw e;
        }
        return false;
    }

    public List<Thumbnail> getPictureThumbnail(int from, int amount, Long folderId) {
        pictures = pictureRep.getPictures(from, amount, folderId);
        List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
        for (int i = 0; i < pictures.size(); i++) {
            thumbnails.add(pictures.get(i).getThumbnail());
        }
        return thumbnails;
    }

    public Picture getPictureInfoById(Long id) {
        Picture picture = this.getPictureByThumbnail(id);
        if (picture == null) {
            try {
                Picture pic = pictureRep.findPictureByThumbnailId(id);
                return pic;
            } catch (NoResultException e) {
                throw e;
            }
        } else {
            return picture;
        }
    }

    private Picture getPictureByThumbnail(Long id) {
        for (int i = 0; i < pictures.size(); i++) {
            if (pictures.get(i).getThumbnail().getId().equals(id)) {
                return pictures.get(i);
            }
        }
        return null;
    }

    public List<Tag> getPictureTags(Long id) {
        Picture picture = this.getPictureByThumbnail(id);
        if (picture.getTags().size() == 0) {
            return null;
        } else {
            List<Tag> tags = picture.getTags();
            return tags;
        }
    }

    public int getPicturesCount(Long folderId) {
        return pictureRep.getPicturesCount(folderId);
    }

}
