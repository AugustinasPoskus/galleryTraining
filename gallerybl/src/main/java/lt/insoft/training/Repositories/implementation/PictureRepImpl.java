package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
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

    public Picture findPictureByThumbnailId(Long id) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Picture> criteria = builder.createQuery(Picture.class);
        Root<Picture> from = criteria.from(Picture.class);
        criteria.select(from);
        criteria.where(builder.equal(from.get(Picture_.thumbnail), id));
        TypedQuery<Picture> typedQuery = manager.createQuery(criteria);
        Picture picture = typedQuery.getSingleResult();
//        CriteriaBuilder builder = manager.getCriteriaBuilder();
//        CriteriaQuery<Picture> criteria = builder.createQuery(Picture.class);
//        Root<Picture> from = criteria.from(Picture.class);
//        From join = from.join("pictureData");
//        criteria.select(from);
//        criteria.where(builder.equal(join.get("id"), id));
//        TypedQuery<Picture> typed = manager.createQuery(criteria);
//        Picture picture = typed.getSingleResult();
        return picture;
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

    @Transactional
    public List<Picture> getPictures(int from, int amount, Long folderId) {
        if ((from >= 0) && (amount > 0)) {
            CriteriaBuilder builder = manager.getCriteriaBuilder();
            CriteriaQuery<Picture> c = builder.createQuery(Picture.class);
            Root<Picture> role = c.from(Picture.class);
            Subquery<Folder> sq = c.subquery(Folder.class);
            Root<Folder> userSQ = sq.from(Folder.class);
            sq.select(userSQ).where(builder.equal(userSQ.get(Folder_.id), folderId));
            c.select(role).where(builder.equal(role.get(Picture_.folder),sq));
            role.fetch(Picture_.thumbnail);
            TypedQuery<Picture> typedQuery = manager.createQuery(c);
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(amount);
            List<Picture> pictures = typedQuery.getResultList();
            //List <Picture> resultList = manager.createQuery(c).getResultList();

            return pictures;
        }
        return null;
    }
}
