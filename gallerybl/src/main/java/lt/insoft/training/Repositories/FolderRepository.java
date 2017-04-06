package lt.insoft.training.Repositories;

import lt.insoft.training.model.Folder;

import java.util.List;


public interface FolderRepository {
    public List<Folder> getFolders(int from, int amount);
    public Folder getFolderById(Long id);
    public boolean removeFolder(Long id);
    public boolean addFolder(Folder folder);
    public boolean updateFolder(Long id, String name);
    public int getFoldersCount();
    public Folder getFolder(Long id);
}
