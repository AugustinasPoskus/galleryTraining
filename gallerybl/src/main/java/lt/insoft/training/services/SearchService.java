package lt.insoft.training.services;

import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureSearchFilter;
import lt.insoft.training.model.Tag;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.repositories.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service("searchService")
public class SearchService {
    @Autowired
    private PictureRepository pictureRep;

    @Autowired
    private TagService tagService;

    @Transactional
    public List<Thumbnail> searchThumbnails(int from, int amount, PictureSearchFilter so) {
        List<String> tagList = so.getPictureTags();
        List<Tag> tags = new ArrayList<>();
        if (tagList != null) {
            try {
                tagList = new ArrayList<String>(new LinkedHashSet<String>(tagList));
                for (String tagName : tagList) {
                    Tag tag = tagService.findTag(tagName);
                    tags.add(tag);
                }
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return pictureRep.findPictureWithParameters(from, amount, so, tags);
    }

    @Transactional
    public Picture findFullPicture(Long id) {
        Picture pic = pictureRep.findPictureByThumbnailId(id);
        return pic;
    }

    @Transactional
    public int getSearchPicturesCount(PictureSearchFilter so) {
        List<String> tagList = so.getPictureTags();
        List<Tag> tags = new ArrayList<>();
        if (tagList != null) {
            for (int i = 0; i < tagList.size(); i++) {
                tagList.set(i, tagList.get(i).trim());

            }
            tagList = new ArrayList<String>(new LinkedHashSet<String>(tagList));
            for (String tagName : tagList) {
                Tag tag = tagService.findTag(tagName);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            if (tags.size() == 0) {
                return 0;
            }
        }
        return pictureRep.getSearchPicturesCount(so, tags);
    }

}
