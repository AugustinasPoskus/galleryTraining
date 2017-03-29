package lt.insoft.training.services;

import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pictureService")
@Scope(value="singleton")
public class PictureService {
    @Autowired
    private PictureRepository pictureRep;

    public PictureService(){}

    public Picture getPicture(Long id)
    {
        return pictureRep.getPicture(id);
    }
    public Long addPicture(Picture picture)
    {
        return pictureRep.insertPicture(picture);
    }
    public boolean removeFolder(Long id)
    {
        return pictureRep.removePicture(id);

    }
}
