package info.bluefoot.watchlater.service;

import info.bluefoot.watchlater.model.Playlist;
import info.bluefoot.watchlater.model.Video;

import java.util.List;

public interface PlaylistService {
    public List<Playlist> getUsersPlaylists(String username);

    public List<Video> getVideosFromPlaylist(Integer id);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);
    
    public void addPlaylistAndVideos(Playlist playlist);
}
