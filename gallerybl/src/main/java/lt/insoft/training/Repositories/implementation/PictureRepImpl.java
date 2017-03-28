package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.insoft.training.model.Picture;

import java.util.List;

public class PictureRepImpl implements PictureRepository {


    @Override
    public List<Picture> getPictures(int from, int amount) {
        return null;
    }

    @Override
    public Picture getPicture(Long id) {
        return null;
    }

    @Override
    public boolean updatePicture(Picture picture) {
        return false;
    }

    @Override
    public boolean insertPicture(Picture picture) {
        return false;
    }

    @Override
    public boolean removePicture(Picture picture) {
        return false;
    }
}
