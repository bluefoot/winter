/*
 * Copyright 2015 Gewton Jhames <bluefoot.dev@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.bluefoot.winter.model;

import java.util.List;

public class Playlist {
    private Integer playlistId;
    private String name;
    private String image;
    private Video lastReproduced;
    private User user;
    private List<Video> videos;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
