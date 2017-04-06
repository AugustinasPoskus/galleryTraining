package lt.insoft.training.Repositories;

import lt.insoft.training.model.Thumbnail;

import java.util.List;

public interface ThumbnailRepository {
    public List<Thumbnail> getThumbnails(int from, int amount,Long folderId);
    public boolean removeThumbnail(Long id);
    //public Long insertThumbnail(Thumbnail thumbnail);
}
