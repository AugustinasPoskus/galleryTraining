package lt.insoft.training.repositories;


import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureSearchFilter;
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
    public List<Thumbnail> findPictureWithParameters(int from, int amount, PictureSearchFilter searchObject, List<Tag> tags);
    public List<Picture> findPictureWithTags(PictureSearchFilter so);
    public int getSearchPicturesCount(PictureSearchFilter so, List<Tag> tags);
}