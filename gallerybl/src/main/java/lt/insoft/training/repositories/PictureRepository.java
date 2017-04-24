package lt.insoft.training.repositories;


import lt.insoft.training.model.Picture;
import lt.insoft.training.model.SearchPictureObject;
import lt.insoft.training.model.Tag;
import lt.insoft.training.model.Thumbnail;

import java.util.List;

public interface PictureRepository {

    public Picture getPicture(Long id);
    public Picture updatePicture(Picture picture);
    public Long insertPicture(Picture picture);
    public boolean removePicture(Long id);
    public Picture findPictureByThumbnailId(Long id);
    public List<Picture> getPictures(int from, int amount, Long folderId);
    public int getPicturesCount(Long folderId);
    public List<Thumbnail> findPictureWithParameters(SearchPictureObject searchObject);
    public List<Picture> findPictureWithTags(SearchPictureObject so);
}