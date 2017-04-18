package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.TagRepository;
import lt.insoft.training.model.Folder;
import lt.insoft.training.model.Folder_;
import lt.insoft.training.model.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagRepImpl implements TagRepository{

    @PersistenceContext
    private EntityManager manager;

    public List<Tag> getAllTags() {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> fromSql = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> select = criteriaQuery.select(fromSql);
        TypedQuery<Tag> typedQuery = manager.createQuery(select);
        List<Tag> tags = typedQuery.getResultList();
        return tags;
    }

    public boolean insertTag(Tag tag){
        try{
            manager.persist(tag);
            return true;
        }catch (PersistenceException pe){
            return false;
        }
    }
}
