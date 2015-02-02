package info.bluefoot.watchlater.service.impl;

import info.bluefoot.watchlater.dao.PlaylistDao;
import info.bluefoot.watchlater.model.Playlist;
import info.bluefoot.watchlater.model.Video;
import info.bluefoot.watchlater.service.PlaylistService;
import info.bluefoot.watchlater.service.VideoService;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    
    @Inject
    private PlaylistDao playlistDao;
    
    @Inject
    private VideoService videoService;
    
    @Override
    public List<Playlist> getUsersPlaylists(String username) {
        return playlistDao.getPlaylistsByUser(username);
    }

    @Override
    public List<Video> getVideosFromPlaylist(Integer id) {
        return playlistDao.getVideosFromPlaylist(id);
    }

    @Override
    public void updateLastPlayedVideo(Integer playlistId, Integer videoId) {
        playlistDao.updateLastPlayedVideo(playlistId, videoId);
    }

    @Override
    public void addPlaylistAndVideos(Playlist playlist) {
        for (Video video : playlist.getVideos()) {
            videoService.addVideo(video);
        }
        playlistDao.addPlaylistAndVideoAssociation(playlist);
    }

}
