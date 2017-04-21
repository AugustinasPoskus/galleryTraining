package lt.insoft.training.repositories.implementation;

import lt.insoft.training.model.Tag_;
import lt.insoft.training.repositories.TagRepository;
import lt.insoft.training.model.Tag;
import org.hibernate.exception.ConstraintViolationException;
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
public class TagRepImpl implements TagRepository {

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

    @Transactional
    public Tag insertTag(Tag tag) {
        manager.persist(tag);
        return tag;
    }

    @Transactional
    public List<Tag> findTag(String name) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
        Root<Tag> from = criteria.from(Tag.class);
        criteria.select(from);
        criteria.where(builder.equal(from.get(Tag_.name), name));
        TypedQuery<Tag> typed = manager.createQuery(criteria);
        return typed.getResultList();
    }

}
