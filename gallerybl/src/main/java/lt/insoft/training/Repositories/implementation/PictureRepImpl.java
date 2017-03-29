package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.model.Picture;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

    @Override
    @Transactional(readOnly=true)
    public Picture getPicture(Long id) {
         return manager.find(Picture.class, id);
    }

    @Override
    @Transactional
    public boolean updatePicture(Picture picture) {
        try{
            manager.merge(picture);
            return true;
        } catch (PersistenceException pe){
            return false;
        }
    }

    @Override
    @Transactional
    public Long insertPicture(Picture picture) {
        try {
            Date date = new Date();
            picture.setDate(date);
            manager.persist(picture);
            return picture.getId();
        } catch (PersistenceException pe) {
            return 0L;
        }
    }

    @Override
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
