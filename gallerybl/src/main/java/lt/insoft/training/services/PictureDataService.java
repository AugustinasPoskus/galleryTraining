package lt.insoft.training.services;


import lt.insoft.training.Repositories.PictureDataRepository;
import lt.insoft.training.model.PictureData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pictureDataService")
@Scope(value="singleton")
public class PictureDataService {
    @Autowired
    private PictureDataRepository pictureDataRep;

    public PictureDataService(){}

    public PictureData getPictureData(Long id)
    {
        return pictureDataRep.getPictureData(id);
    }

    public List<PictureData> getPictureThumbnail(int from, int amount)
    {
        return pictureDataRep.getPicturesThumbnails(from, amount);
    }

    public Long addPictureData(PictureData pictureData)
    {
        return pictureDataRep.insertPictureData(pictureData);
    }
    public boolean removeFolder(Long id)
    {
        return pictureDataRep.removePictureData(id);
    }


}