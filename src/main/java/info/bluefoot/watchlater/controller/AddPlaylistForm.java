package info.bluefoot.watchlater.controller;

import info.bluefoot.watchlater.model.Playlist;

public class AddPlaylistForm {
    private Playlist playlist;
    private String videos;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

}
