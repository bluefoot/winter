package info.bluefoot.winter.model;

import java.util.List;

public class Playlist {
    private Integer playlistId;
    private String name;
    private String image;
    private Video lastReproduced;
    private WinterUser user;
    private List<Video> videos;

    public WinterUser getUser() {
        return user;
    }

    public void setUser(WinterUser user) {
        this.user = user;
    }

    public Video getLastReproduced() {
        return lastReproduced;
    }

    public void setLastReproduced(Video lastReproduced) {
        this.lastReproduced = lastReproduced;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public Playlist setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        for (Video video : videos) {
            video.setPlaylist(this);
        }
        this.videos = videos;
    }

}
