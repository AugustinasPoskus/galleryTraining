package repositories;

import lt.insoft.training.model.Folder;
import lt.insoft.training.model.Tag;
import lt.insoft.training.repositories.TagRepository;
import org.hibernate.exception.ConstraintViolationException;
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
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/META-INF/applicationContextTest.xml")
public class TagRepTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    @Transactional
    public void testAddTag() {
        Tag tag = new Tag();
        tag.setName("tag");
        Tag addedTag = tagRepository.insertTag(tag);
        Assert.assertEquals(tag.getName(), addedTag.getName());
    }

    @Test
    @Transactional
    public void testAddEmptyTag() {
        Tag tag = new Tag();
        Tag addedTag = tagRepository.insertTag(tag);
        Assert.assertEquals(null, addedTag);

        Assert.assertEquals(null, tagRepository.insertTag(null));

        tag = new Tag();
        tag.setName("");
        Assert.assertEquals(null, tagRepository.insertTag(tag));
    }

    @Test(expected = JpaSystemException.class)
    @Transactional
    public void testDuplicatedTags() {
        Tag tag = new Tag();
        tag.setName("tag");
        tagRepository.insertTag(tag);
        Tag tag1 = new Tag();
        tag1.setName("tag");
        tagRepository.insertTag(tag1);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @Transactional
    public void testfindOneTag() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setName("tag" + i);
            tagRepository.insertTag(tag);
        }
        List<Tag> tags =  tagRepository.findTag("tag" + (count-1));
        Assert.assertEquals(1, tags.size());
    }

    @Test
    @Transactional
    public void testFindNotExistingTag(){
        List<Tag> tags =  tagRepository.findTag("tag");
        Assert.assertEquals(0, tags.size());
    }

    @Test
    @Transactional
    public void testFindNotExistingTag2(){
        List<Tag> tags =  tagRepository.findTag(null);
        Assert.assertEquals(0, tags.size());
    }

    @Test
    @Transactional
    public void testGetAllTags(){
        int count = 15;
        List<Tag> tagsList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setName("tag" + i);
            tagRepository.insertTag(tag);
            tagsList.add(tag);
        }
        List<Tag> tags =  tagRepository.getAllTags();
        Assert.assertEquals(tagsList, tags);
    }

    @Test
    public void testGetAllTags2(){
        List<Tag> tags =  tagRepository.getAllTags();
        List tagsList = new ArrayList();
        Assert.assertEquals(tagsList, tags);
    }

}
