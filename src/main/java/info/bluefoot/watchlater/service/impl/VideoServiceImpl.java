package info.bluefoot.watchlater.service.impl;

import java.util.List;

import info.bluefoot.watchlater.dao.VideoDao;
import info.bluefoot.watchlater.model.Video;
import info.bluefoot.watchlater.service.VideoService;

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
        v.setLastPlayedTime(lastPLayedTime);
        videoDao.updateNotNullValues(v);
        // TODO Auto-generated method stub

    }
    @Override
    public void addVideo(Video video) {
        int videoId = videoDao.insert(video);
        video.setVideoId(videoId);
    }
    @Override
    public List<Video> getAll() {
        return videoDao.getAll();
    }

}
