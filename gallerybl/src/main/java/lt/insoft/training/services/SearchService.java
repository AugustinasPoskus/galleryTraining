package lt.insoft.training.services;

import lt.insoft.training.model.SearchPictureObject;
import lt.insoft.training.model.Tag;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.repositories.PictureRepository;
import lt.insoft.training.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Thumbnail> searchThumbnails(SearchPictureObject so) {
        List<String> tagList = so.getPictureTags();
        if(tagList != null){
            for (int i = 0; i < tagList.size(); i++) {
                tagList.set(i, tagList.get(i).trim());
            }
            tagList = new ArrayList<String>(new LinkedHashSet<String>(tagList));
        }
        so.setPictureTags(tagList);
        return pictureRep.findPictureWithParameters(so);
    }
}
