package info.bluefoot.winter.service;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.User;

import java.util.List;

public interface PlaylistService {
    public List<Playlist> getPlaylistsFromUser(User user);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);
    
    public void addPlaylistAndVideos(Playlist playlist);

    public void removePlaylistAndVideos(Integer playlistId);

    public Playlist getPlaylistByIdAndUser(Integer playlistId,
            User principal);
}
