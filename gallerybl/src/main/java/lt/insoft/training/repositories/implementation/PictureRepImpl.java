package lt.insoft.training.repositories.implementation;

import lt.insoft.training.repositories.PictureRepository;
import lt.insoft.training.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        } catch (NoResultException e) {
            throw e;
        }
    }

    @Transactional
    public Picture updatePicture(Picture picture) {
        Picture mergedPicture = manager.merge(picture);
        return mergedPicture;
    }

    @Transactional
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
            Root<Picture> pictureRoot = c.from(Picture.class);
            Subquery<Folder> sq = c.subquery(Folder.class);
            Root<Folder> folderRoot = sq.from(Folder.class);
            sq.select(folderRoot).where(builder.equal(folderRoot.get(Folder_.id), folderId));
            c.select(pictureRoot).where(builder.equal(pictureRoot.get(Picture_.folder), sq)).orderBy(builder.asc(pictureRoot.get(Picture_.date)));
            pictureRoot.fetch(Picture_.thumbnail);
            pictureRoot.fetch(Picture_.pictureData);
            pictureRoot.fetch(Picture_.tags, JoinType.LEFT);
            TypedQuery<Picture> typedQuery = manager.createQuery(c);
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(amount);
            List<Picture> pictures = typedQuery.getResultList();

            return pictures;
        }
        return null;
    }

    public int getPicturesCount(Long folderId) {
        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> c = qb.createQuery(Long.class);
        Root<Picture> pictureRoot = c.from(Picture.class);
        Subquery<Folder> sq = c.subquery(Folder.class);
        Root<Folder> folderRoot = sq.from(Folder.class);
        sq.select(folderRoot).where(qb.equal(folderRoot.get(Folder_.id), folderId));
        c.select(qb.count(pictureRoot)).where(qb.equal(pictureRoot.get(Picture_.folder), sq));
        return (int) (long) manager.createQuery(c).getSingleResult();
    }

    public List<Thumbnail> findPictureWithParameters(SearchObject so) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Thumbnail> query = cb.createQuery(Thumbnail.class);
        Root<Picture> picture = query.from(Picture.class);
        Join<Picture, Thumbnail> thumbs = picture.join("thumbnail");
        query.select(thumbs);
        if (so.getPictureName() != null) {
            query.where(cb.equal(picture.get(Picture_.name), so.getPictureName()));
        }
        if (so.getPictureInsertDate() != null) {
            Date date;
            Date datePlusOne = new Date(so.getPictureInsertDate().getTime() + +TimeUnit.DAYS.toMillis(1));
            query.select(thumbs).where(cb.between(picture.get(Picture_.date), so.getPictureInsertDate(), datePlusOne));
        }
        if (so.getPictureDescription() != null) {
            query.where(cb.equal(picture.get(Picture_.description), so.getPictureDescription()));
        }
        if (so.getPictureQuality() != null) {
            query.where(cb.equal(picture.get(Picture_.quality), so.getPictureQuality()));
        }
        if(so.getSort() != null) {
            if (so.getSort().equals("Ascending")) {
                query.orderBy(cb.asc(picture.get(Picture_.date)));
            } else if (so.getSort().equals("Descending")) {
                query.orderBy(cb.desc(picture.get(Picture_.date)));
            }
        }
//        if (so.getSearchBy().equals(SearchBy.TAGS)){
//            query.select(thumbs).where(cb.equal(picture.get(Picture_.tags), so.getEnteredText()));
//        }
        TypedQuery<Thumbnail> typedQuery = manager.createQuery(query);
        List<Thumbnail> thumbnails = typedQuery.getResultList();
        return thumbnails;
    }
}