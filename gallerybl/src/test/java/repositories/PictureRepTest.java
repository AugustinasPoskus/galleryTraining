package repositories;

import lt.insoft.training.model.*;
import lt.insoft.training.repositories.FolderRepository;
import lt.insoft.training.repositories.PictureRepository;
import lt.insoft.training.repositories.TagRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/META-INF/applicationContextTest.xml")
public class PictureRepTest {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private TagRepository tagRepository;
    @PersistenceContext
    private EntityManager entityManager;
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
        entityManager.flush();
    }

    @Test(expected = PersistenceException.class)
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
        entityManager.flush();
    }

    @Test(expected = PersistenceException.class)
    @Transactional
    public void testAddEmptyPicture() {
        pictureRepository.insertPicture(new Picture());
    }

    @Test
    @Transactional
    public void testGetPicture() {
        int index = 6;
        List<Picture> pictures = this.createPictures(6);
        Picture picture = pictures.get(index / 2);
        Picture foundPicture = pictureRepository.getPicture(picture.getId());
        Assert.assertEquals(picture, foundPicture);
    }

    @Test
    @Transactional
    public void testGetNotExistingPicture() {
        Picture foundPicture = pictureRepository.getPicture(300L);
        Assert.assertEquals(null, foundPicture);
    }

    @Test
    @Transactional
    public void testUpdatePictureName() {
        String updateName = "updatedName";
        Picture picture = this.createPictures(1).get(0);
        picture.setName(updateName);
        pictureRepository.updatePicture(picture);
        Picture updated = pictureRepository.getPicture(picture.getId());
        Assert.assertEquals(updateName, updated.getName());
    }

    @Test
    @Transactional
    public void testUpdatePictureTags() {
        int count = 4;
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setName("tag" + i);
            tagRepository.insertTag(tag);
            tagList.add(tag);
        }
        Picture picture = this.createPictures(1).get(0);
        picture.setTags(tagList);
        pictureRepository.updatePicture(picture);
        Picture updated = pictureRepository.getPicture(picture.getId());
        Assert.assertEquals(tagList, updated.getTags());
    }

    @Test
    @Transactional
    public void testUpdatePictureDescription() {
        String updateDescription = "updatedDescription";
        Picture picture = this.createPictures(1).get(0);
        picture.setDescription(updateDescription);
        pictureRepository.updatePicture(picture);
        Picture updated = pictureRepository.getPicture(picture.getId());
        Assert.assertEquals(updateDescription, updated.getDescription());
    }

    @Test
    @Transactional
    public void testUpdatePictureQuality() {
        String updateQuality = "updatedQuality";
        Picture picture = this.createPictures(1).get(0);
        picture.setQuality(updateQuality);
        pictureRepository.updatePicture(picture);
        Picture updated = pictureRepository.getPicture(picture.getId());
        Assert.assertEquals(updateQuality, updated.getQuality());
    }

    @Test
    @Transactional
    public void testRemovePicture() {
        int insertCount = 6;
        int index = 6;
        List<Picture> pictures = this.createPictures(insertCount);
        Picture picture = pictures.get(index / 2);
        Assert.assertEquals(true, pictureRepository.removePicture(picture.getId()));
        Picture foundPicture = pictureRepository.getPicture(picture.getId());
        Assert.assertEquals(null, foundPicture);
    }

    @Test(expected = EntityNotFoundException.class)
    @Transactional
    public void testRemoveNotExistingPicture() {
        pictureRepository.removePicture(30L);
    }

    @Test
    @Transactional
    public void testFindPictureByThumbnailId() {
        int insertCount = 6;
        int index = 6;
        List<Picture> pictures = this.createPictures(insertCount);
        Picture picture = pictures.get(index / 2);
        Picture foundPicture = pictureRepository.findPictureByThumbnailId(picture.getThumbnail().getId());
        Assert.assertEquals(picture, foundPicture);
    }

    @Test(expected = NoResultException.class)
    @Transactional
    public void testFindPictureByNotExistingThumbnailId() {
        Picture foundPicture = pictureRepository.findPictureByThumbnailId(30L);
    }

    @Test
    @Transactional
    public void testGetPicturesCount() {
        int count = 11;
        this.createPictures(count);
        int picturesCount = pictureRepository.getPicturesCount(folder.getId());
        Assert.assertEquals(count, picturesCount);
    }

    @Test
    @Transactional
    public void testGetEmptyPicturesCount() {
        int picturesCount = pictureRepository.getPicturesCount(folder.getId());
        Assert.assertEquals(0, picturesCount);
    }

    @Test
    @Transactional
    public void testGetPicturesCountInOherFolder() {
        int count = 11;
        this.createPictures(count);
        Folder otherFolder = new Folder();
        otherFolder.setDate(new Date());
        otherFolder.setName("otherFolder");
        folderRepository.addFolder(otherFolder);
        int picturesCount = pictureRepository.getPicturesCount(otherFolder.getId());
        Assert.assertEquals(0, picturesCount);
    }

    @Test
    @Transactional
    public void testGetPicturesWithWrongParamters() {
        List<Picture> pictureList = pictureRepository.getPictures(-1, 10, folder.getId());
        Assert.assertEquals(null, pictureList);
    }

    @Test
    @Transactional
    public void testGetThumbnails(){
        int pictureCount = 5;
        List<Picture> pictures = this.createPictures(pictureCount);
        List<Thumbnail> foundThumbanils = pictureRepository.getThumbnails(0, pictureCount, folder.getId());
        List<Thumbnail> thumbnails = pictures.stream().map(Picture::getThumbnail).collect(Collectors.toList());
        Assert.assertEquals(thumbnails, foundThumbanils);
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

    @Transactional
    public List<Picture> createPictures(int count) {
        List<Picture> pictureList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Picture picture = new Picture();
            picture.setName("name");
            picture.setDescription("description");
            picture.setQuality("6x6");
            Date date = new Date(System.currentTimeMillis() + 3600 * i * 1000);
            picture.setDate(date);

            picture.setFolder(folder);

            PictureData pictureData = new PictureData();
            pictureData.setData(this.getFileBytes());
            picture.setPictureData(pictureData);

            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setData(this.getFileBytes());
            picture.setThumbnail(thumbnail);
            pictureRepository.insertPicture(picture);
            pictureList.add(picture);
        }
        return pictureList;
    }

}