package lt.insoft.training.repositories;

import lt.insoft.training.model.PictureData;


public interface PictureDataRepository {
    public boolean removePictureData(Long id);
    public PictureData getPictureData(Long id);
}
