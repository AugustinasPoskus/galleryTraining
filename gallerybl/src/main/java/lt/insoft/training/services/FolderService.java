package lt.insoft.training.services;

import lt.insoft.training.Repositories.FolderRepository;
import lt.insoft.training.Repositories.implementation.FolderRepImp;
import lt.insoft.training.model.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service("folderService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class FolderService implements IFolderService,Serializable {


    @Autowired
    private FolderRepository folderRep;

    public FolderService(){
        folderRep = new FolderRepImp();
    }

    public List<Folder> getAllFolders(){
        return folderRep.getAllFolders();
    }

    public boolean addFolder(Folder folder){
        return folderRep.addFolder(folder);
    }

    public void check(){
        System.out.println("hey");
    }

}
