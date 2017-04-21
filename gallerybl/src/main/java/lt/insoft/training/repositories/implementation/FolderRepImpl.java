package lt.insoft.training.repositories.implementation;

import lt.insoft.training.repositories.FolderRepository;
import lt.insoft.training.model.Folder;
import lt.insoft.training.model.Folder_;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class FolderRepImpl implements FolderRepository {

    @PersistenceContext
    private EntityManager manager;

    public Folder getFolder(Long id) {
        if (!id.equals(null)) {
            return manager.find(Folder.class, id);
        }
        return null;
    }

    public List<Folder> getFolders(int from, int amount) {
        if ((from >= 0) && (amount > 0)) {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<Folder> criteriaQuery = criteriaBuilder.createQuery(Folder.class);
            Root<Folder> fromSql = criteriaQuery.from(Folder.class);
            CriteriaQuery<Folder> select = criteriaQuery.select(fromSql);
            select.orderBy(criteriaBuilder.asc(fromSql.get(Folder_.date)));
            TypedQuery<Folder> typedQuery = manager.createQuery(select);
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(amount);
            List<Folder> folders = typedQuery.getResultList();
            return folders;
        }
        return null;
    }

    public boolean removeFolder(Long id) {
        try {
            Folder folder = manager.getReference(Folder.class, id);
            manager.remove(folder);
            return true;
        } catch (PersistenceException pe) {
            return false;
        }
    }

    public boolean addFolder(Folder folder) {
        if (!folder.getName().equals(null)) {
            manager.persist(folder);
            manager.flush();
            return true;
        }
        return false;
    }

    public Folder updateFolder(Folder folder, String name) {
        Folder folderRef = manager.getReference(Folder.class, folder.getId());
        try {
            folder.setName(name);
            Folder newFolder = manager.merge(folder);
            return newFolder;
        } catch (PersistenceException lockEx) {
            return null;
        }
    }

    public int getFoldersCount() {
        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(Folder.class)));
        return (int) (long) manager.createQuery(cq).getSingleResult();
    }
}
