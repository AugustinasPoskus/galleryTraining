package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PictureRepImpl implements PictureRepository {

    @PersistenceContext
    private EntityManager manager;

    public Picture getPicture(Long id) {
         return manager.find(Picture.class, id);
    }

    public Picture findPictureByThumbnailId(Long id) {
        try {
            CriteriaBuilder builder = manager.getCriteriaBuilder();
            CriteriaQuery<Picture> criteria = builder.createQuery(Picture.class);
            Root<Picture> from = criteria.from(Picture.class);
            criteria.select(from);
            criteria.where(builder.equal(from.get(Picture_.thumbnail), id));
            TypedQuery<Picture> typedQuery = manager.createQuery(criteria);
            Picture picture = typedQuery.getSingleResult();
            return picture;
        }catch (NoResultException e){
            throw e;
        }
    }


    public boolean updatePicture(Picture picture) {
        try{
            manager.merge(picture);
            return true;
        } catch (PersistenceException pe){
            return false;
        }
    }

    public Long insertPicture(Picture picture) {
            manager.persist(picture);
            return picture.getId();
    }

    public boolean removePicture(Long id) {
        Picture pictureInstance = manager.getReference(Picture.class, id);
        if (pictureInstance != null) {
            manager.remove(pictureInstance);
            return true;
        }
        return false;
    }

    public List<Picture> getPictures(int from, int amount, Long folderId) {
        if ((from >= 0) && (amount > 0)) {
            CriteriaBuilder builder = manager.getCriteriaBuilder();
            CriteriaQuery<Picture> c = builder.createQuery(Picture.class);
            Root<Picture> role = c.from(Picture.class);
            Subquery<Folder> sq = c.subquery(Folder.class);
            Root<Folder> userSQ = sq.from(Folder.class);
            sq.select(userSQ).where(builder.equal(userSQ.get(Folder_.id), folderId));
            c.select(role).where(builder.equal(role.get(Picture_.folder),sq)).orderBy(builder.asc(role.get(Picture_.date)));;
            role.fetch(Picture_.thumbnail);
            role.fetch(Picture_.pictureData);
            role.fetch(Picture_.tags, JoinType.LEFT);
            TypedQuery<Picture> typedQuery = manager.createQuery(c);
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(amount);
            List<Picture> pictures = typedQuery.getResultList();

            return pictures;
        }
        return null;
    }
    public int getPicturesCount(Long folderId){
        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> c = qb.createQuery(Long.class);
        Root<Picture> role = c.from(Picture.class);
        Subquery<Folder> sq = c.subquery(Folder.class);
        Root<Folder> userSQ = sq.from(Folder.class);
        sq.select(userSQ).where(qb.equal(userSQ.get(Folder_.id), folderId));
        c.select(qb.count(role)).where(qb.equal(role.get(Picture_.folder),sq));
        return (int) (long) manager.createQuery(c).getSingleResult();
    }
}
