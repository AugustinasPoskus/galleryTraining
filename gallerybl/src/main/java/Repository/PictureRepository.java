package Repository;

import lt.insoft.training.insoft.training.model.Picture;
import java.util.List;

public interface PictureRepository {

    public List<Picture> getAllPictures();
    public Picture getPicture(Long id);
    public boolean updatePicture(Picture picture);
    public boolean insertPicture(Picture picture);
    public boolean removePicture(Picture picture);

}