package lt.insoft.training.repositories.implementation;

import lt.insoft.training.repositories.PictureDataRepository;
import lt.insoft.training.model.PictureData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PictureDataRepImpl implements PictureDataRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<PictureData> getPicturesThumbnails(int from, int amount) {
        if ((from >= 0) && (amount > 0)) {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<PictureData> criteriaQuery = criteriaBuilder.createQuery(PictureData.class);
            Root<PictureData> fromSql = criteriaQuery.from(PictureData.class);
            CriteriaQuery<PictureData> select = criteriaQuery.select(fromSql);
            TypedQuery<PictureData> typedQuery = manager.createQuery(select);
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(amount);
            List<PictureData> pictureData = typedQuery.getResultList();
            return pictureData;
        }
        return null;
    }

    public boolean removePictureData(Long id) {
        return false;
    }

    public PictureData getPictureData(Long id) {
        return manager.find(PictureData.class, id);
    }

}
