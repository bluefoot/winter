package info.bluefoot.watchlater.model;

import java.util.List;

public class Playlist  {
    private int playlistId;
    private String name;
    private List<Video> videos;
    private Video lastPlayed;
    private String user;
    
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Video getLastPlayed() {
        return lastPlayed;
    }
    public void setLastPlayed(Video lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
    private String image;
    
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getPlaylistId() {
        return playlistId;
    }
    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
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
        this.videos = videos;
    }
    
    
}
