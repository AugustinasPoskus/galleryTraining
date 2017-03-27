package lt.insoft.training.Repositories;

import lt.insoft.training.model.Folder;

import java.util.List;


public interface FolderRepository {
    public List<Folder> getAllFolders();
    public Folder getFolderById(Integer id);
    public boolean removeFolder(Integer id);
    public boolean addFolder(Folder folder);
}
