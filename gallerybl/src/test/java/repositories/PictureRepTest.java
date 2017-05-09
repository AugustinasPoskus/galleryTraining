package repositories;

import lt.insoft.training.model.*;
import lt.insoft.training.repositories.FolderRepository;
import lt.insoft.training.repositories.PictureRepository;
import org.hibernate.PropertyValueException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import sun.misc.IOUtils;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/META-INF/applicationContextTest.xml")
public class PictureRepTest {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private FolderRepository folderRepository;

    private Folder folder;

    @Before
    public void initialize() {
        folder = new Folder();
        folder.setDate(new Date());
        folder.setName("folder");
        folderRepository.addFolder(folder);
    }

    @Test
    @Transactional
    public void testAddPicture() {
        Picture picture = new Picture();
        picture.setName("name");
        picture.setDescription("description");
        picture.setQuality("6x6");
        picture.setDate(new Date());

        picture.setFolder(folder);

        Tag tag = new Tag();
        tag.setName("tag");

        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        picture.setTags(tagList);

        PictureData pictureData = new PictureData();
        pictureData.setData(this.getFileBytes());
        picture.setPictureData(pictureData);

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setData(this.getFileBytes());
        picture.setThumbnail(thumbnail);
        Long id = pictureRepository.insertPicture(picture);
        Assert.assertEquals(picture.getId(), id);
    }

    @Test(expected = PersistenceException.class)
    @Transactional
    public void testAddPictureWithoutFolder() {
        Picture picture = new Picture();
        picture.setName("name");
        picture.setDescription("description");
        picture.setQuality("6x6");
        picture.setDate(new Date());

        Tag tag = new Tag();
        tag.setName("tag");

        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        picture.setTags(tagList);

        PictureData pictureData = new PictureData();
        pictureData.setData(this.getFileBytes());
        picture.setPictureData(pictureData);

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setData(this.getFileBytes());
        picture.setThumbnail(thumbnail);
        pictureRepository.insertPicture(picture);
    }

    @Test(expected = PersistenceException.class)
    @Transactional
    public void testAddPictureWithoutThumbnail() {
        Picture picture = new Picture();
        picture.setName("name");
        picture.setDescription("description");
        picture.setQuality("6x6");
        picture.setDate(new Date());

        Tag tag = new Tag();
        tag.setName("tag");

        picture.setFolder(folder);

        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        picture.setTags(tagList);

        PictureData pictureData = new PictureData();
        pictureData.setData(this.getFileBytes());
        picture.setPictureData(pictureData);

        Long id = pictureRepository.insertPicture(picture);
        System.out.println(picture.getPictureData().getId());
    }

    @Test
    @Transactional
    public void testAddPictureWithoutPictureData() {
        Picture picture = new Picture();
        picture.setName("name");
        picture.setDescription("description");
        picture.setQuality("6x6");
        picture.setDate(new Date());

        picture.setFolder(folder);

        Tag tag = new Tag();
        tag.setName("tag");

        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        picture.setTags(tagList);

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setData(this.getFileBytes());
        picture.setThumbnail(thumbnail);

        pictureRepository.insertPicture(picture);
    }

    private byte[] getFileBytes() {
        try {
            Path path = Paths.get("/resources/testPicture.jpg");
            byte[] bytes = Files.readAllBytes(path);
            return bytes;
        } catch (IOException e) {
            return new byte[0];
        }
    }


}
//    public Picture getPicture(Long id);
//    public Picture updatePicture(Picture picture);
//    public Long insertPicture(Picture picture);
//    public boolean removePicture(Long id);
//    public Picture findPictureByThumbnailId(Long id);
//    public List<Picture> getPictures(int from, int amount, Long folderId);
//    public int getPicturesCount(Long folderId);
//    public List<Thumbnail> findPictureWithParameters(int from, int amount, PictureSearchFilter searchObject, List<Tag> tags);
//    public List<Picture> findPictureWithTags(PictureSearchFilter so);
//    public int getSearchPicturesCount(PictureSearchFilter so, List<Tag> tags);