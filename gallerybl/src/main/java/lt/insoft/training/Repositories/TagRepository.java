package lt.insoft.training.Repositories;

import lt.insoft.training.model.Tag;

import java.util.List;

public interface TagRepository {
    public List<Tag> getAllTags();
    public boolean insertTag(Tag tag);
}
