package info.bluefoot.winter.dao;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.User;

import java.util.List;

public interface PlaylistDao {
    public List<Playlist> getPlaylistsByUser(User user);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);

    public Playlist addPlaylist(Playlist playlist);

    public void removePlaylist(Integer playlistId);

    public Playlist getPlaylistsByIdAndUser(Integer playlistId, String username);
}
