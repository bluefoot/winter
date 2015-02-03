package info.bluefoot.winter.dao;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.WinterUser;

import java.util.List;

public interface PlaylistDao {
    public List<Playlist> getPlaylistsByUser(WinterUser user);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);

    public Playlist addPlaylist(Playlist playlist);
}
