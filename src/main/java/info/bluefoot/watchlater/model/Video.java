package info.bluefoot.watchlater.model;

public class Video {
    private int videoId;
    private String url;
    private Integer lastPlayedTime;
    
    public Video(int videoId) {
        this.videoId = videoId;
    }
    
    public Video(String url) {
        this.url = url;
    }
    
    public Video() {
        super();
    }

    public Integer getLastPlayedTime() {
        return lastPlayedTime;
    }
    public void setLastPlayedTime(Integer lastPlayedTime) {
        this.lastPlayedTime = lastPlayedTime;
    }
    public int getVideoId() {
        return videoId;
    }
    public void setVideoId(int videoId) {
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
                videoId, url, lastPlayedTime);
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
