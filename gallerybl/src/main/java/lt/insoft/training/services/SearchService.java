package lt.insoft.training.services;

import lt.insoft.training.model.SearchObject;
import lt.insoft.training.model.Thumbnail;
import lt.insoft.training.repositories.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("searchService")
public class SearchService {
    @Autowired
    private PictureRepository pictureRep;

    @Transactional
    public List<Thumbnail> searchThumbnails(SearchObject so){
        return pictureRep.findPictureWithParameters(so);
    }
}
