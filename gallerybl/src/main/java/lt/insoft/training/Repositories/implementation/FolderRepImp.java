package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.FolderRepository;
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
public class FolderRepImp implements FolderRepository {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public List<Folder> getFolders(int from, int amount) {
        if((from >=0) && (amount > 0)) {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<Folder> criteriaQuery = criteriaBuilder.createQuery(Folder.class);
            Root<Folder> fromSql = criteriaQuery.from(Folder.class);
            CriteriaQuery<Folder> select = criteriaQuery.select(fromSql);
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
        Folder folderInstance = this.getFolderById(id);
        if (folderInstance != null) {
            manager.remove(folderInstance);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Folder getFolderById(Long id) {
        if(!id.equals(null)){
            return manager.find(Folder.class, id);
        }
        return null;
    }

    @Transactional
    public boolean addFolder(Folder folder) {
        if (!folder.getName().equals(null)) {
            Date date = new Date();
            folder.setDate(date);
            manager.persist(folder);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateFolder(Long id, String name) {
        try {
            Folder folder = this.getFolderById(id);
            folder.setName(name);
            manager.merge(folder);
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
