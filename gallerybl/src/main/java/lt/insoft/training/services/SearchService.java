package lt.insoft.training.services;

import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureSearchFilter;
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
    private TagService TagService;

    @Transactional
    public List<Thumbnail> searchThumbnails(int from, int amount, PictureSearchFilter so) {
        List<String> tagList = so.getPictureTags();
        if(tagList != null){
            for (int i = 0; i < tagList.size(); i++) {
                tagList.set(i, tagList.get(i).trim());
            }
            tagList = new ArrayList<String>(new LinkedHashSet<String>(tagList));
        }
        so.setPictureTags(tagList);
        return pictureRep.findPictureWithParameters(from, amount, so);
    }

    @Transactional
    public Picture findFullPicture(Long id){
        try {
            Picture pic = pictureRep.findPictureByThumbnailId(id);
            return pic;
        } catch (NoResultException e) {
            throw e;
        }
    }

    @Transactional
    public int getSearchPicturesCount(PictureSearchFilter so) {
        return pictureRep.getSearchPicturesCount(so);
    }
}
