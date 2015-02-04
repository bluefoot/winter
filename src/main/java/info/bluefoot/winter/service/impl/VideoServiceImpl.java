package info.bluefoot.winter.service.impl;

import java.util.List;

import info.bluefoot.winter.dao.VideoDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.service.VideoService;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Inject
    private VideoDao videoDao;
    
    @Override
    public void updateLastPlayedTime(int videoId, Integer lastPLayedTime) {
        Video v = new Video();
        v.setVideoId(videoId);
        v.setCurrentPosition(lastPLayedTime);
        videoDao.updateNotNullValues(v);
    }
    
    @Override
    public void addVideo(Video video) {
        int videoId = videoDao.insert(video);
        video.setVideoId(videoId);
    }

    @Override
    public List<Video> getVideosFromPlaylist(Playlist playlist) {
        return videoDao.getVideosFromPlaylist(playlist);
    }

    @Override
    public void removeVideosFromPlaylist(Integer playlistId) {
        videoDao.removeVideosFromPlaylist(playlistId);
    }
}
