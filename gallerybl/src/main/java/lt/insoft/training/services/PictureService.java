package lt.insoft.training.services;

import lt.insoft.training.Repositories.PictureDataRepository;
import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.Repositories.ThumbnailRepository;
import lt.insoft.training.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("pictureService")
@Scope(value = "singleton")
public class PictureService {
    @Autowired
    private PictureRepository pictureRep;
    @Autowired
    private PictureDataRepository pictureDataRep;
    @Autowired
    private ThumbnailRepository thumbnailRep;

    @Transactional
    public boolean addPicture(Picture picture, PictureData pictureData, Thumbnail thumbnail, Folder folder){
        picture.setPictureData(pictureData);
        picture.setThumbnail(thumbnail);
        picture.setFolder(folder);
        Date date = new Date();
        picture.setDate(date);
        pictureRep.insertPicture(picture);
        return true;
    }

    public Picture getPictureByDataId(Long id) {
        return pictureRep.findPictureByThumbnailId(id);
    }

    public boolean removePicture(Long id) {
        pictureDataRep.removePictureData(id);
        pictureRep.removePicture(id);
        return false;
    }

    public PictureData getPictureData(Long id) {
        return pictureDataRep.getPictureData(id);
    }

    public List<Thumbnail> getPictureThumbnail(int from, int amount, Long folderId) {
        System.out.println("from: " + from + " amount: " + amount);
        return pictureRep.getThumbnails(from, amount,folderId);
    }

}
