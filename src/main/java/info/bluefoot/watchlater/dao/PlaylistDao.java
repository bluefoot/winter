package info.bluefoot.watchlater.dao;

import info.bluefoot.watchlater.model.Playlist;
import info.bluefoot.watchlater.model.Video;

import java.util.List;

public interface PlaylistDao {
    public List<Playlist> getPlaylistsByUser(String username);

    public List<Video> getVideosFromPlaylist(Integer id);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);

    public void addPlaylistAndVideoAssociation(Playlist playlist); 
}
