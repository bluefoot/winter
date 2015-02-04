package info.bluefoot.winter.dao;

import java.util.List;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;

public interface VideoDao {
    public void updateNotNullValues(Video video);

    public List<Video> getVideosFromPlaylist(Playlist playlist);

    public int insert(Video video);

    public void removeVideosFromPlaylist(Integer playlistId);
}
