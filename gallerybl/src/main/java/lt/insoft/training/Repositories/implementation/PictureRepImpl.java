package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.model.Picture;
import lt.insoft.training.model.PictureData;
import lt.insoft.training.model.PictureData_;
import lt.insoft.training.model.Picture_;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class PictureRepImpl implements PictureRepository {

    private EntityManager manager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.manager = entityManager;
    }

    @Transactional(readOnly=true)
    public Picture getPicture(Long id) {
         return manager.find(Picture.class, id);
    }

    @Transactional(readOnly=true)
    public PictureData getPictureData(Long id) {
        return manager.find(PictureData.class, id);
    }

    public Picture findPictureByDataId(Long id) {
//        PictureData pd = this.getPictureData(id);
//        CriteriaBuilder builder = manager.getCriteriaBuilder();
//        CriteriaQuery<Picture> criteria = builder.createQuery(Picture.class);
//        Root<Picture> from = criteria.from(Picture.class);
//        criteria.select(from);
//        criteria.where(builder.equals(from.get(PictureData_.id), pd));
//        TypedQuery<User> typed = manager.createQuery(criteria);
        return null;
    }

    @Transactional
    public boolean updatePicture(Picture picture) {
        try{
            manager.merge(picture);
            return true;
        } catch (PersistenceException pe){
            return false;
        }
    }

    @Transactional
    public Long insertPicture(Picture picture) {
        try {
            manager.persist(picture);
            return picture.getId();
        } catch (PersistenceException pe) {
            return 0L;
        }
    }

    @Transactional
    public boolean removePicture(Long id) {
        Picture pictureInstance = this.getPicture(id);
        if (pictureInstance != null) {
            manager.remove(pictureInstance);
            return true;
        }
        return false;
    }
}
