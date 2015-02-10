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

public class Video {
    private Integer videoId;
    private Integer currentPosition;
    private String url;
    private Integer sortPosition;
    private Playlist playlist;

    public Integer getSortPosition() {
        return sortPosition;
    }

    public void setSortPosition(Integer sortPosition) {
        this.sortPosition = sortPosition;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Video setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        return this;
    }

    public Video(int videoId) {
        this.videoId = videoId;
    }

    public Video(String url, Integer sortPosition) {
        this.url = url;
        this.sortPosition=sortPosition;
    }

    public Video() {
        super();
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("Video [videoId=%s, url=%s, lastPlayedTime=%s]",
                videoId, url, currentPosition);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Video other = (Video) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
