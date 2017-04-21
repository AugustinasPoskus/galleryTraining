package lt.insoft.training.repositories.implementation;


import lt.insoft.training.repositories.ThumbnailRepository;
import lt.insoft.training.model.Thumbnail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ThumbnailRepImpl implements ThumbnailRepository {

    @PersistenceContext
    private EntityManager manager;

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
            return null;
        }
        return null;
    }

    public boolean removeThumbnail(Long id) {
        return false;
    }

//    public Long insertThumbnail(Thumbnail thumbnail) {
//        return null;
//    }
}
