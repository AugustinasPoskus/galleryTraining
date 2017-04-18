package lt.insoft.training.services;

import lt.insoft.training.Repositories.TagRepository;
import lt.insoft.training.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service("tagService")
public class TagService {

    @Autowired
    private TagRepository tagRep;
    List<Tag> tags;

    public TagService(){}

    @PostConstruct
    private void getAllTags(){
        tags = tagRep.getAllTags();
    }

    @Transactional
    public synchronized Tag addTag(String name){
        Tag tag = this.contains(name);
        if(tag != null){
            return tag;
        }else{
            Tag newTag = new Tag();
            newTag.setName(name);
            if(tagRep.insertTag(newTag)){
                tags.add(newTag);
            }
            return newTag;
        }
    }

    public Tag contains(String name){
        for (Tag tag:tags) {
            if(tag.getName().equals(name)){
                return tag;
            }
        }
        return null;
    }

}
