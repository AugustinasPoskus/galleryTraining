package lt.insoft.training.Repositories.implementation;

import lt.insoft.training.Repositories.FolderRepository;
import lt.insoft.training.model.Folder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class FolderRepImp implements FolderRepository {

    private EntityManager manager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.manager = entityManager;
    }

    @Transactional(readOnly=true)
    public List<Folder> getAllFolders()
    {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Folder> cq = cb.createQuery(Folder.class);
        Root<Folder> from = cq.from(Folder.class);
        cq.select(from);
        TypedQuery<Folder> q = manager.createQuery(cq);
        List<Folder> folders = q.getResultList();

        //List<Folder> folders = manager.createQuery("Select f From Folder f", Folder.class).getResultList();
        return folders;
    }

    @Transactional
    public boolean removeFolder(Long id)
    {
        Folder folderInstance = this.getFolderById(id);
        if (folderInstance != null) {
            manager.remove(folderInstance);
            return true;
        }
        return false;
    }

    @Transactional(readOnly=true)
    public Folder getFolderById(Long id)
    {
        return manager.find(Folder.class, id);
    }

    @Transactional
    public boolean addFolder(Folder folder)
    {
        if(!folder.getName().equals(null))
        {
            Date date = new Date();
            folder.setDate(date);
            manager.persist(folder);
            return true;
        }
        return false;
    }

}
