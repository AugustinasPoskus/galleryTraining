package lt.insoft.training.services;

import lt.insoft.training.Repositories.PictureDataRepository;
import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.Repositories.ThumbnailRepository;
import lt.insoft.training.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("pictureService")
@Scope(value = "singleton")
public class PictureService {
    @Autowired
    private PictureRepository pictureRep;
    @Autowired
    private PictureDataRepository pictureDataRep;
    @Autowired
    private ThumbnailRepository thumbnailRep;
    private List<Picture> pictures;

    public PictureService(){}

    @Transactional
    public long addPicture(Picture picture, PictureData pictureData, Thumbnail thumbnail, Folder folder, List<String> tagList){
        List<Tag> tags = new ArrayList<Tag>();
        for (String val : tagList) {
            val = val.trim();
            if(!val.isEmpty()){
                Tag tag = new Tag();
                tag.setName(val);
                tags.add(tag);
            }
        }
        picture.setTags(tags);
        picture.setPictureData(pictureData);
        picture.setThumbnail(thumbnail);
        picture.setFolder(folder);
        Date date = new Date();
        picture.setDate(date);
        pictures.add(picture);
        return pictureRep.insertPicture(picture);
    }

    @Transactional
    public boolean removePictureByThumbnail(Long id) {
        Picture picture = this.getPictureByThumbnail(id);
        if(pictureRep.removePicture(picture.getId())){
            pictures.remove(picture);
            return true;
        }
        return false;
    }

    public PictureData getPictureData(Long id) {
        return pictureDataRep.getPictureData(id);
    }

    public List<Thumbnail> getPictureThumbnail(int from, int amount, Long folderId) {
        pictures = pictureRep.getPictures(from, amount,folderId);
        List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
        for(int i=0; i<pictures.size();i++)
        {
            thumbnails.add(pictures.get(i).getThumbnail());
        }
        return thumbnails;
    }

    public Picture getPictureInfoById(Long id) {
        Picture picture = this.getPictureByThumbnail(id);
        if(picture == null) {
            return pictureRep.findPictureByThumbnailId(id);
        } else{
            return picture;
        }

    }

    private Picture getPictureByThumbnail(Long id){
        for(int i=0; i<pictures.size();i++)
        {
            if(pictures.get(i).getThumbnail().getId().equals(id)){
                return pictures.get(i);
            }
        }
        return null;
    }

    public List<Tag> getPictureTags(Long id){
        Picture picture = this.getPictureByThumbnail(id);
        if(picture.getTags().size() == 0){
            System.out.println("saaad");
            return null;
        }else{
            List<Tag> tags = picture.getTags();
            for(int i=0; i< tags.size() ;i++){
                System.out.println(tags.get(i).getName());
            }
            return tags;
        }
    }
}
