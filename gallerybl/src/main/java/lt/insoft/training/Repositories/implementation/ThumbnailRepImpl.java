package lt.insoft.training.Repositories.implementation;


import lt.insoft.training.Repositories.ThumbnailRepository;
import lt.insoft.training.model.Thumbnail;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class ThumbnailRepImpl implements ThumbnailRepository{

    @PersistenceContext
    private EntityManager manager;

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
