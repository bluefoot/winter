package info.bluefoot.watchlater.service;

import java.util.List;

import info.bluefoot.watchlater.model.Video;

public interface VideoService {
    public void updateLastPlayedTime(int videoId, Integer lastPLayedTime);

    public void addVideo(Video video);

    public List<Video> getAll();
}
