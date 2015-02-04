package info.bluefoot.winter.service.impl;

import info.bluefoot.winter.dao.PlaylistDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.model.WinterUser;
import info.bluefoot.winter.service.PlaylistService;
import info.bluefoot.winter.service.VideoService;

import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    
    @Inject
    private PlaylistDao playlistDao;
    
    @Inject
    private VideoService videoService;
    
    @Override
    public List<Playlist> getPlaylistsFromUser(WinterUser user) {
        List<Playlist> playlists = playlistDao.getPlaylistsByUser(user);
        for (Playlist playlist : playlists) {
            playlist.setUser(user);
        }
        return playlists;
    }

    @Override
    public void updateLastPlayedVideo(Integer playlistId, Integer videoId) {
        playlistDao.updateLastPlayedVideo(playlistId, videoId);
    }

    @Override
    public void addPlaylistAndVideos(Playlist playlist) {
        playlistDao.addPlaylist(playlist);
        for (Video video : playlist.getVideos()) {
            videoService.addVideo(video);
        }
    }

    @Override
    public void removePlaylistAndVideos(Integer playlistId) {
        videoService.removeVideosFromPlaylist(playlistId);
        playlistDao.removePlaylist(playlistId);
    }

    @Override
    public Playlist getPlaylistByIdAndUser(Integer playlistId,
            WinterUser principal) {
        try {
            return playlistDao.getPlaylistsByIdAndUser(playlistId, principal.getUserId());
        } catch (EmptyResultDataAccessException e) {
            throw new PlaylistNotFoundException(playlistId.toString(), e);
        }
    }

}
