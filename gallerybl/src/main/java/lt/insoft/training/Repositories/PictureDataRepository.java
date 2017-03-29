package lt.insoft.training.Repositories;

import lt.insoft.training.model.PictureData;

import java.util.List;

/**
 * Created by augustinas.poskus on 2017-03-29.
 */
public interface PictureDataRepository {
    public List<PictureData> getPicturesData(int from, int amount);
    public boolean removePictureData(Long id);
    public PictureData getPictureData(Long id);
    public Long insertPictureData(PictureData pictureData);
}
