package lt.insoft.training.services;

import lt.insoft.training.repositories.TagRepository;
import lt.insoft.training.model.Tag;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service("tagService")
public class TagService {

    @Autowired
    private TagRepository tagRep;

    public TagService() {
    }

    @Transactional
    public Tag addTag(String name) {
        List<Tag> tags;
        Tag tag = new Tag();
        tag.setName(name);
        tags = tagRep.findTag(tag.getName());
        if (tags.isEmpty()) {
            tag = tagRep.insertTag(tag);
            return tag;
        } else {
            return tags.get(0);
        }
    }

    @Transactional
    public Tag findTag(String name) {
        List<Tag> tags;
        Tag tag = new Tag();
        tag.setName(name);
        tags = tagRep.findTag(tag.getName());
        if (tags.isEmpty()) {
            return null;
        } else {
            return tags.get(0);
        }
    }

}
