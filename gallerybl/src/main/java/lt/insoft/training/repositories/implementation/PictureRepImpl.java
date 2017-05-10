package lt.insoft.training.repositories.implementation;

import lt.insoft.training.model.*;
import lt.insoft.training.repositories.PictureRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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

    @Transactional
    public boolean removePicture(Long id) {
        Picture pictureInstance = manager.getReference(Picture.class, id);
        if (pictureInstance == null) {
            return false;
        }
        manager.remove(pictureInstance);
        return true;
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

    public List<Thumbnail> findPictureWithParameters(int from, int amount, PictureSearchFilter so, List<Tag> tags) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Thumbnail> query = cb.createQuery(Thumbnail.class);
        Root<Picture> picture = query.from(Picture.class);
        Join<Picture, Thumbnail> thumbs = picture.join(Picture_.thumbnail);
        query.select(thumbs);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (so.getPictureName() != null && !so.getPictureName().equals("")) {
            predicates.add(cb.and(cb.equal(picture.get(Picture_.name), so.getPictureName())));
        }
        if (so.getPictureInsertDate() != null && !so.getPictureInsertDate().equals("")) {
            Date datePlusOne = new Date(so.getPictureInsertDate().getTime() + +TimeUnit.DAYS.toMillis(1));
            predicates.add(cb.and(cb.between(picture.get(Picture_.date), so.getPictureInsertDate(), datePlusOne)));
        }
        if (so.getPictureDescription() != null && !so.getPictureDescription().equals("")) {
            predicates.add(cb.and(cb.equal(picture.get(Picture_.description), so.getPictureDescription())));
        }
        if (so.getPictureQuality() != null && !so.getPictureQuality().equals("")) {
            predicates.add(cb.and(cb.equal(picture.get(Picture_.quality), so.getPictureQuality())));
        }
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                predicates.add(cb.isMember(tags.get(i), picture.get(Picture_.tags)));
            }
        }
        if (so.getSort() != null) {
            if (so.getSort().equals("Asc")) {
                query.orderBy(cb.asc(picture.get(Picture_.date)));
            } else if (so.getSort().equals("Dsc")) {
                query.orderBy(cb.desc(picture.get(Picture_.date)));
            }
        }
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<Thumbnail> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(amount);
        List<Thumbnail> thumbnails = typedQuery.getResultList();
        return thumbnails;
    }

    public int getSearchPicturesCount(PictureSearchFilter so, List<Tag> tags) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> c = cb.createQuery(Long.class);
        Root<Picture> picture = c.from(Picture.class);
        Join<Picture, Thumbnail> thumbs = picture.join(Picture_.thumbnail);
        c.select(cb.count(picture));
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (so.getPictureName() != null && !so.getPictureName().equals("")) {
            predicates.add(cb.and(cb.equal(picture.get(Picture_.name), so.getPictureName())));
        }
        if (so.getPictureInsertDate() != null && !so.getPictureInsertDate().equals("")) {
            Date datePlusOne = new Date(so.getPictureInsertDate().getTime() + +TimeUnit.DAYS.toMillis(1));
            predicates.add(cb.and(cb.between(picture.get(Picture_.date), so.getPictureInsertDate(), datePlusOne)));
        }
        if (so.getPictureDescription() != null && !so.getPictureDescription().equals("")) {
            predicates.add(cb.and(cb.equal(picture.get(Picture_.description), so.getPictureDescription())));
        }
        if (so.getPictureQuality() != null && !so.getPictureQuality().equals("")) {
            predicates.add(cb.and(cb.equal(picture.get(Picture_.quality), so.getPictureQuality())));
        }
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                predicates.add(cb.isMember(tags.get(i), picture.get(Picture_.tags)));
            }
        }
        c.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return (int) (long) manager.createQuery(c).getSingleResult();
    }

    public List<Thumbnail> getThumbnails(int from, int amount, Long folderId) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Thumbnail> query = cb.createQuery(Thumbnail.class);
        Root<Picture> picture = query.from(Picture.class);
        Join<Picture, Thumbnail> thumbs = picture.join(Picture_.thumbnail);
        query.select(thumbs)
                .where(cb.equal(picture.get(Picture_.folder), manager.getReference(Folder.class, folderId)))
                .orderBy(cb.asc(picture.get(Picture_.date)));
        TypedQuery<Thumbnail> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(amount);
        List<Thumbnail> thumbnails = typedQuery.getResultList();
        return thumbnails;
    }

}
