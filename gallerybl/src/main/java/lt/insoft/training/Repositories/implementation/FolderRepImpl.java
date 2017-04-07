package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.FolderRepository;
import lt.insoft.training.model.Folder;
import lt.insoft.training.model.Folder_;
import lt.insoft.training.model.Picture;
import lt.insoft.training.model.Picture_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
@Transactional
public class FolderRepImpl implements FolderRepository {

    @PersistenceContext
    private EntityManager manager;

    public Folder getFolder(Long id){
        return manager.find(Folder.class, id);
    }
    @Transactional
    public List<Folder> getFolders(int from, int amount) {
        if ((from >= 0) && (amount > 0)) {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<Folder> criteriaQuery = criteriaBuilder.createQuery(Folder.class);
            Root<Folder> fromSql = criteriaQuery.from(Folder.class);
            CriteriaQuery<Folder> select = criteriaQuery.select(fromSql);
            select.orderBy(criteriaBuilder.desc(fromSql.get(Folder_.date)));
            TypedQuery<Folder> typedQuery = manager.createQuery(select);
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(amount);
            List<Folder> folders = typedQuery.getResultList();
            return folders;
        }
        return null;
    }

    @Transactional
    public boolean removeFolder(Long id) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        CriteriaDelete<Picture> query = cb.createCriteriaDelete(Picture.class);
        Root<Picture> root = query.from(Picture.class);
        query.where(cb.equal(root.get(Picture_.folder), id));
        manager.createQuery(query).executeUpdate();

        CriteriaDelete<Folder> delete = cb.createCriteriaDelete(Folder.class);

        Root e = delete.from(Folder.class);
        delete.where(cb.equal(e.get("id"), id));
        int effectedRows = manager.createQuery(delete).executeUpdate();
        if (effectedRows <= 0) {
            return false;
        } else if(effectedRows > 0){
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Folder getFolderById(Long id) {
        if (!id.equals(null)) {
            return manager.find(Folder.class, id);
        }
        return null;
    }

    @Transactional
    public boolean addFolder(Folder folder) {
        if (!folder.getName().equals(null)) {
            manager.persist(folder);
            manager.flush();
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateFolder(Long id, String name) {
        try {
            CriteriaBuilder cb = manager.getCriteriaBuilder();
            CriteriaUpdate<Folder> update = cb.createCriteriaUpdate(Folder.class);
            Root e = update.from(Folder.class);
            update.set("name", name);
            update.where(cb.equal(e.get("id"), id));
            this.manager.createQuery(update).executeUpdate();
            return true;
        } catch (PersistenceException pe) {
            return false;
        }
    }

    public int getFoldersCount() {
        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(Folder.class)));
        return (int) (long) manager.createQuery(cq).getSingleResult();
    }

}
