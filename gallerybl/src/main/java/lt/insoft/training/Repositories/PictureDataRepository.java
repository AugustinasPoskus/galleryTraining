package lt.insoft.training.Repositories;

import lt.insoft.training.model.PictureData;


public interface PictureDataRepository {
    public boolean removePictureData(Long id);
    public PictureData getPictureData(Long id);
    //public Long insertPictureData(PictureData pictureData);
}
