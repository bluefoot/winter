package info.bluefoot.winter.service;

import java.util.List;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;

public interface VideoService {
    public void updateLastPlayedTime(int videoId, Integer lastPLayedTime);

    public void addVideo(Video video);

    public List<Video> getVideosFromPlaylist(Playlist playlist);

}
