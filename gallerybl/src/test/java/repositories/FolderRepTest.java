package repositories;

import lt.insoft.training.model.Folder;
import lt.insoft.training.repositories.FolderRepository;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/META-INF/applicationContextTest.xml")
public class FolderRepTest {

    @Autowired
    private FolderRepository folderRep;

    @Test
    @Transactional
    public void testAddFolder() {
        Folder folder = new Folder();
        Date date = new Date();
        folder.setDate(date);
        folder.setName("vardas");
        Assert.assertEquals(true, folderRep.addFolder(folder));
    }

    @Test
    @Transactional
    public void testAddFolderWithoutDate() {
        Folder folderWithoutDate = new Folder();
        folderWithoutDate.setName("name");
        boolean isInserted = folderRep.addFolder(folderWithoutDate);
        Assert.assertEquals(false, isInserted);
    }

    @Test
    @Transactional
    public void testAddFolderWithoutName() {
        Folder folderWithoutName = new Folder();
        folderWithoutName.setDate(new Date());
        boolean isInserted = folderRep.addFolder(folderWithoutName);
        Assert.assertEquals(false, isInserted);
    }

    @Test
    @Transactional
    public void testAddFolderWithoutFields() {

        Folder folderWithoutName = new Folder();
        boolean isInserted = folderRep.addFolder(folderWithoutName);
        Assert.assertEquals(false, isInserted);
    }

    @Test
    @Transactional
    public void testAddTwoSameFolders() {
        int count = folderRep.getFoldersCount();
        Folder folder = new Folder();
        folder.setName("name");
        folder.setDate(new Date());
        folderRep.addFolder(folder);
        folderRep.addFolder(folder);
        List<Folder> folders = folderRep.getFolders(0, 10);
        Assert.assertEquals(count + 1, folders.size());
    }

    @Test
    @Transactional
    public void testRemoveFolder() {
        Assert.assertEquals(false, folderRep.removeFolder(100L));
        Folder folder = new Folder();
        folder.setDate(new Date());
        folder.setName("name");
        folderRep.addFolder(folder);
        folderRep.removeFolder(folder.getId());
        Assert.assertEquals(0, folderRep.getFolders(0, 10).size());

        Folder folder1 = new Folder();
        folder1.setDate(new Date());
        folder1.setName("name1");
        folderRep.addFolder(folder1);
        folderRep.removeFolder(folder.getId());
        Assert.assertEquals(1, folderRep.getFolders(0, 10).size());
    }

    @Test
    @Transactional
    public void testUpdateFolder() {
        String updateName = "updated";
        Folder folder = new Folder();
        Date date = new Date();
        folder.setDate(date);
        folder.setName("vardas");
        folderRep.addFolder(folder);
        folderRep.updateFolder(folder, updateName);
        Assert.assertEquals(updateName, folderRep.getFolder(folder.getId()).getName());
    }

    @Test(expected = JpaSystemException.class)
    @Transactional
    public void testUpdateWithInvalidFolder() {
        String updateName = "updated";
        Folder folder1 = new Folder();
        folderRep.updateFolder(folder1, updateName);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @Transactional
    public void testUpdateFolderWithoutName() {
        Folder folder = new Folder();
        folder.setName("name");
        folder.setDate(new Date());
        folderRep.addFolder(folder);
        Folder updatedFolder = folderRep.updateFolder(folder, null);
        Assert.assertEquals(null, updatedFolder);
    }

    @Test
    @Transactional
    public void testGetFolder() {
        Long id = 100L;
        Folder folder = folderRep.getFolder(id);
        Assert.assertEquals(null, folder);

        folder = new Folder();
        folder.setName("name");
        folder.setDate(new Date());
        folderRep.addFolder(folder);

        Folder foundFolder = folderRep.getFolder(folder.getId());
        Assert.assertEquals(folder.getName(), foundFolder.getName());
    }

    @Test
    @Transactional
    public void testFolderCount() {
        int count = 20;
        for (int i = 0; i < count; i++) {
            Assert.assertEquals(i, folderRep.getFoldersCount());
            Folder folder = new Folder();
            folder.setName("Aaaa");
            folder.setDate(new Date());
            folderRep.addFolder(folder);
        }
        Assert.assertEquals(count, folderRep.getFoldersCount());
    }

    @Test
    @Transactional
    public void testGetFolderList() {
        int size = folderRep.getFoldersCount();
        int addCount = 20;
        int skip = 5;
        int take = 10;
        List<Folder> foldersAspected = new ArrayList<>();
        for (int i = 0; i < addCount; i++) {
            Folder folder = new Folder();
            folder.setName("Aaaa");
            folder.setDate(new Date());
            folderRep.addFolder(folder);
            foldersAspected.add(folder);
        }
        List<Folder> folders = folderRep.getFolders(size, addCount);
        Assert.assertEquals(foldersAspected, folders);
        List<Folder> filteredList = foldersAspected.stream().skip(skip).limit(take).collect(Collectors.toList());
        folders = folderRep.getFolders(size + skip, take);
        Assert.assertEquals(filteredList, folders);
    }

    @Test(expected = JpaSystemException.class)
    @Transactional
    public void testUpdateFolderWithOptimisticLocking() {
        Folder folder = new Folder();
        folder.setName("name");
        folder.setDate(new Date());
        folderRep.addFolder(folder);

        Folder first = folderRep.getFolder(folder.getId());
        Folder second = folderRep.getFolder(folder.getId());
        folderRep.updateFolder(first, "first");
        TestTransaction.flagForCommit();
        TestTransaction.end();
        folderRep.updateFolder(second, "second");
    }

    @Test
    @Transactional
    public void testGetFolderWithIdNull(){
        Assert.assertEquals(null, folderRep.getFolder(null));
    }

    @Test
    @Transactional
    public void testGetFoldersListWithWrongParams(){
        Assert.assertEquals(null, folderRep.getFolders(-1,0));
    }
}
