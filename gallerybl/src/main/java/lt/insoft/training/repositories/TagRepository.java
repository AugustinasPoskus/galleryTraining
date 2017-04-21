package lt.insoft.training.repositories;

import lt.insoft.training.model.Tag;

import java.util.List;

public interface TagRepository {
    public List<Tag> getAllTags();
    public Tag insertTag(Tag tag);
    public List<Tag> findTag(String name);
}
