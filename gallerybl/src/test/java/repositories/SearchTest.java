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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/META-INF/applicationContextTest.xml")
public class SearchTest {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private TagRepository tagRepository;
    private Folder folder;
    private List<Thumbnail> thumbnails;
    private List<Picture> pictures;
    private int count = 23;

    @Before
    public void initialize() {
        thumbnails = new ArrayList<>();
        pictures = new ArrayList<>();
        folder = new Folder();
        folder.setDate(new Date());
        folder.setName("folder");
        folderRepository.addFolder(folder);
        for (int i = 0; i < count; i++) {
            Picture picture = new Picture();
            picture.setName("name" + i);
            picture.setDescription("description" + i);
            picture.setQuality("6x6");
            picture.setFolder(folder);

            PictureData pictureData = new PictureData();
            pictureData.setData(this.getFileBytes());
            picture.setPictureData(pictureData);

            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setData(this.getFileBytes());
            picture.setThumbnail(thumbnail);

            Date date = new Date(System.currentTimeMillis() + 3600 * i * 1000);
            picture.setDate(date);
            pictureRepository.insertPicture(picture);
            thumbnails.add(thumbnail);
            pictures.add(picture);
        }
    }

    @Test
    @Transactional
    public void testSearch() {
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        for (Thumbnail thumbnail : foundThumbnails) {
            Assert.assertEquals(true, thumbnails.contains(thumbnail));
        }
    }

    @Test
    @Transactional
    public void testSearchByName() {
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        searchFilter.setPictureName("name" + (count - 1));
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(1, foundThumbnails.size());
    }

    @Test
    @Transactional
    public void testSearchByDate() {
        Date date = new GregorianCalendar(2017, 11, 10).getTime();
        int index = count / 6;
        Picture picture = pictures.get(index);
        picture.setDate(date);
        pictureRepository.updatePicture(picture);
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        searchFilter.setPictureInsertDate(date);
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(1, foundThumbnails.size());
        Assert.assertEquals(picture.getThumbnail().getId(), foundThumbnails.get(0).getId());
    }

    @Test
    @Transactional
    public void testSearchByQuality(){
        String quality = "99x99";
        int index = count / 6;
        Picture picture = pictures.get(index);
        picture.setQuality(quality);
        pictureRepository.updatePicture(picture);
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        searchFilter.setPictureQuality(quality);
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(1, foundThumbnails.size());
        Assert.assertEquals(picture.getThumbnail().getId(), foundThumbnails.get(0).getId());
    }

    @Test
    @Transactional
    public void testSearchByDescription(){
        String description = "descriptionUpdated";
        int index = count / 6;
        Picture picture = pictures.get(index);
        picture.setDescription(description);
        pictureRepository.updatePicture(picture);
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        searchFilter.setPictureDescription(description);
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(1, foundThumbnails.size());
        Assert.assertEquals(picture.getThumbnail().getId(), foundThumbnails.get(0).getId());
    }

    @Test
    @Transactional
    public void testSearchByTags(){
        Tag first = new Tag();
        Tag second = new Tag();
        first.setName("first");
        second.setName("second");
        tagRepository.insertTag(first);
        tagRepository.insertTag(second);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(first);
        tagList.add(second);
        int index = count / 6;
        Picture picture = pictures.get(index);
        picture.setTags(tagList);
        pictureRepository.updatePicture(picture);
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, tagList);
        Assert.assertEquals(1, foundThumbnails.size());
        Assert.assertEquals(picture.getThumbnail().getId(), foundThumbnails.get(0).getId());
    }

    @Test
    @Transactional
    public void testSearchSortAscending(){
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        searchFilter.setSort("Asc");
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        pictures = pictureRepository.getPictures(0, count, folder.getId());
        Collections.sort(pictures);
        thumbnails = pictures.stream().map(Picture::getThumbnail).collect(Collectors.toList());
        Assert.assertEquals(thumbnails, foundThumbnails);
    }

    @Test
    @Transactional
    public void testSearchSortDescending(){
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        searchFilter.setSort("Dsc");
        List<Thumbnail> foundThumbnails = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        List<Thumbnail> tempThumbnails = new ArrayList<>(thumbnails);
        Collections.reverse(tempThumbnails);
        Assert.assertEquals(tempThumbnails, foundThumbnails);
    }

    @Test
    @Transactional
    public void testSearchPicturesCount(){
        PictureSearchFilter searchFilter = new PictureSearchFilter();
        int size = pictureRepository.getSearchPicturesCount(searchFilter, null);
        List<Thumbnail> foundPictures = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(pictures.size(), size);


        searchFilter = new PictureSearchFilter();
        searchFilter.setPictureName("tag" + count);
        size = pictureRepository.getSearchPicturesCount(searchFilter, null);
        foundPictures = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(foundPictures.size(), size);

        searchFilter = new PictureSearchFilter();
        searchFilter.setPictureDescription("description");
        size = pictureRepository.getSearchPicturesCount(searchFilter, null);
        foundPictures = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(foundPictures.size(), size);

        searchFilter = new PictureSearchFilter();
        searchFilter.setPictureQuality("6x6");
        size = pictureRepository.getSearchPicturesCount(searchFilter, null);
        foundPictures = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(foundPictures.size(), size);

        searchFilter = new PictureSearchFilter();
        searchFilter.setPictureInsertDate(new Date(System.currentTimeMillis() + 3600 * 24000));
        size = pictureRepository.getSearchPicturesCount(searchFilter, null);
        foundPictures = pictureRepository.findPictureWithParameters(0, count, searchFilter, null);
        Assert.assertEquals(foundPictures.size(), size);

        searchFilter = new PictureSearchFilter();
        Tag first = new Tag();
        Tag second = new Tag();
        first.setName("first");
        second.setName("second");
        tagRepository.insertTag(first);
        tagRepository.insertTag(second);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(first);
        tagList.add(second);
        int index = count / 6;
        Picture picture = pictures.get(index);
        picture.setTags(tagList);
        pictureRepository.updatePicture(picture);
        size = pictureRepository.getSearchPicturesCount(searchFilter, tagList);
        foundPictures = pictureRepository.findPictureWithParameters(0, count, searchFilter, tagList);
        Assert.assertEquals(foundPictures.size(), size);
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
