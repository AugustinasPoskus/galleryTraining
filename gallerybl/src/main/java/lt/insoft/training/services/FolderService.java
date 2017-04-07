package lt.insoft.training.services;

import lt.insoft.training.Repositories.FolderRepository;
import lt.insoft.training.model.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("folderService")
@Scope(value = "singleton")
public class FolderService {


    @Autowired
    private FolderRepository folderRep;

    public FolderService() {
    }

    public Folder getFolder(Long id){
        return folderRep.getFolder(id);
    }

    public List<Folder> getFolders(int from, int amount) {
        return folderRep.getFolders(from, amount);
    }

    public boolean addFolder(Folder folder) {
        folder.setName(folder.getName());
        Date date = new Date();
        folder.setDate(date);
        return folderRep.addFolder(folder);
    }

    public boolean removeFolder(Long id) {
        return folderRep.removeFolder(id);
    }

    public boolean updateFolder(Long id, String name) {
        return folderRep.updateFolder(id, name);
    }

    public int foldersCount() {
        return folderRep.getFoldersCount();
    }
}
