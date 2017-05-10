package lt.insoft.training.services;

import lt.insoft.training.repositories.PictureRepository;
import lt.insoft.training.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

@Service("pictureService")
public class PictureService {
    @Autowired
    private PictureRepository pictureRep;

    @Autowired
    private TagService tagService;

    public PictureService() {
    }

    @Transactional
    public long addPicture(Picture picture, PictureData pictureData, Thumbnail thumbnail, Folder folder, List<String> tagList) {
        List<Tag> tags = new ArrayList<Tag>();
        for (int i = 0; i < tagList.size(); i++) {
            tagList.set(i, tagList.get(i).trim());
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
        return pictureRep.insertPicture(picture);
    }

    @Transactional
    public boolean removePictureByThumbnail(Long id) {
        Picture picture = this.getPictureInfoById(id);
        picture.setTags(null);
        if (pictureRep.removePicture(picture.getId())) {
            return true;
        }
        return false;
    }

    public List<Thumbnail> getPictureThumbnail(int from, int amount, Long folderId) {
        List<Thumbnail> thumbnails = pictureRep.getThumbnails(from, amount, folderId);
        return thumbnails;
    }

    @Transactional
    public Picture getPictureInfoById(Long id) {
        Picture pic = pictureRep.findPictureByThumbnailId(id);
        return pic;
    }

    public int getPicturesCount(Long folderId) {
        return pictureRep.getPicturesCount(folderId);
    }

    @Transactional
    public boolean updatePicture(Picture updatedPic, Long picId, List<String> tagList) {
        List<Tag> tags = new ArrayList<Tag>();
        for (int i = 0; i < tagList.size(); i++) {
            tagList.set(i, tagList.get(i).trim());
        }
        tagList = new ArrayList<String>(new LinkedHashSet<String>(tagList));
        for (String val : tagList) {
            if (!val.isEmpty()) {
                Tag tag = tagService.addTag(val);
                tags.add(tag);
            }
        }
        updatedPic.setTags(tags);
        pictureRep.updatePicture(updatedPic);
        return true;
    }

}
