package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.PictureDataRepository;
import lt.insoft.training.Repositories.PictureRepository;
import lt.insoft.training.insoft.training.model.Picture;
import lt.insoft.training.model.Folder;
import lt.insoft.training.model.PictureData;
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
public class PictureDataRepImp implements PictureDataRepository{

    @PersistenceContext
    private EntityManager manager;


    @Override
    @Transactional(readOnly=true)
    public List<PictureData> getPicturesData(int from, int amount) {
        if((from >=0) && (amount > 0)) {
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

    @Transactional
    public boolean removePictureData(Long id) {
        return false;
    }

    @Transactional(readOnly=true)
    public PictureData getPictureData(Long id) {
        return manager.find(PictureData.class, id);
    }

    @Transactional
    public Long insertPictureData(PictureData pictureData) {
        try {
            manager.persist(pictureData);
            return pictureData.getId();
        } catch (PersistenceException pe) {
            return 0L;
        }
    }
}
