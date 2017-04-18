package lt.insoft.training.services;

import lt.insoft.training.Repositories.FolderRepository;
import lt.insoft.training.model.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("folderService")
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

    @Transactional
    public boolean addFolder(Folder folder) {
        Date date = new Date();
        folder.setDate(date);
        return folderRep.addFolder(folder);
    }

    @Transactional
    public boolean removeFolder(Long id) {
        return folderRep.removeFolder(id);
    }

    @Transactional
    public Folder updateFolder(Folder folder, String name) {
        return folderRep.updateFolder(folder, name);
    }

    public int foldersCount() {
        return folderRep.getFoldersCount();
    }

}
