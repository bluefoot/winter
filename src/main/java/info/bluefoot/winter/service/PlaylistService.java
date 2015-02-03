package info.bluefoot.winter.service;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.WinterUser;

import java.util.List;

public interface PlaylistService {
    public List<Playlist> getPlaylistsFromUser(WinterUser user);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);
    
    public void addPlaylistAndVideos(Playlist playlist);
}
