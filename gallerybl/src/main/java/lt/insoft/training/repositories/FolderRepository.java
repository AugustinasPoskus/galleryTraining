package lt.insoft.training.repositories;

import lt.insoft.training.model.Folder;

import java.util.List;


public interface FolderRepository {
    public List<Folder> getFolders(int from, int amount);
    public boolean removeFolder(Long id);
    public boolean addFolder(Folder folder);
    public Folder updateFolder(Folder folder, String name);
    public int getFoldersCount();
    public Folder getFolder(Long id);
}
