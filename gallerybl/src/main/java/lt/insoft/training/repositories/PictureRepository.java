package lt.insoft.training.repositories;


import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureSearchFilter;
import lt.insoft.training.model.Tag;
import lt.insoft.training.model.Thumbnail;

import java.util.List;

public interface PictureRepository {

    Picture getPicture(Long id);
    Picture updatePicture(Picture picture);
    Long insertPicture(Picture picture);
    boolean removePicture(Long id);
    Picture findPictureByThumbnailId(Long id);
    List<Picture> getPictures(int from, int amount, Long folderId);
    int getPicturesCount(Long folderId);
    List<Thumbnail> findPictureWithParameters(int from, int amount, PictureSearchFilter searchObject, List<Tag> tags);
    int getSearchPicturesCount(PictureSearchFilter so, List<Tag> tags);
    List<Thumbnail> getThumbnails(int from, int amount, Long folderId);
}