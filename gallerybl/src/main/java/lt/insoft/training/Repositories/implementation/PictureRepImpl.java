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
        criteria.where(builder.equal(from.get(Picture_.pictureData), id));
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
    public List<Thumbnail> getThumbnails(int from, int amount, Long folderId) {
        if ((from >= 0) && (amount > 0)) {
//            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//            CriteriaQuery<Thumbnail> criteriaQuery = criteriaBuilder.createQuery(Thumbnail.class);
//            Root<Thumbnail> fromSql = criteriaQuery.from(Thumbnail.class);
//            CriteriaQuery<Thumbnail> select = criteriaQuery.select(fromSql);
//            TypedQuery<Thumbnail> typedQuery = manager.createQuery(select);
//            typedQuery.setFirstResult(from);
//            typedQuery.setMaxResults(amount);
//            List<Thumbnail> thumbnails = typedQuery.getResultList();
            CriteriaBuilder builder = manager.getCriteriaBuilder();
//            CriteriaQuery<Thumbnail> cq = builder.createQuery(Thumbnail.class);
//            Root<Thumbnail> thumbnailRoot = cq.from(Thumbnail.class);
//            Subquery<Picture> sq = cq.subquery(Picture.class);
//            Root<Picture> pictureRoot = cq.from(Picture.class);
//            sq.select(pictureRoot.getModel("Thumbnail"));
//            sq.where(builder.equal(pictureRoot.get("folder"), folderId));
//            cq.select(thumbnailRoot);
//            cq.where(builder.equal(thumbnailRoot.get("id"), sq));

            CriteriaQuery<Tuple> cq = builder.createTupleQuery();
            Root<Picture> root = cq.from(Picture.class);
            cq.multiselect(root.get(Picture_.id), root.get(Picture_.folder));  //using metamodel
            List<Tuple> tupleResult = manager.createQuery(cq).getResultList();
            for (Tuple t : tupleResult) {
                Long id = (Long) t.get(0);
                Folder version = (Folder) t.get(1);
                System.out.println("picture id: " + id + " folder" + version.getId());
            }
        }
        return null;
    }
}
