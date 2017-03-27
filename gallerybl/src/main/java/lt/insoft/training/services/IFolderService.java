package lt.insoft.training.services;

import lt.insoft.training.model.Folder;

import java.util.List;

public interface IFolderService {
    public List<Folder> getAllFolders();
    public boolean addFolder(Folder folder);
    public void check();
}
