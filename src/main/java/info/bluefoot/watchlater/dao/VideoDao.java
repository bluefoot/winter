package info.bluefoot.watchlater.dao;

import java.util.List;

import info.bluefoot.watchlater.model.Video;

public interface VideoDao {
    public void updateNotNullValues(Video video);

    public int insert(Video video);

    public List<Video> getAll();
}
