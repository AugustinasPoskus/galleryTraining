package lt.insoft.training.Repositories;


import lt.insoft.training.model.Picture;
import lt.insoft.training.model.Thumbnail;

import java.util.List;

public interface PictureRepository {

    public Picture getPicture(Long id);
    public boolean updatePicture(Picture picture);
    public Long insertPicture(Picture picture);
    public boolean removePicture(Long id);
    public Picture findPictureByThumbnailId(Long id);
    public List<Picture> getPictures(int from, int amount, Long folderId);
}