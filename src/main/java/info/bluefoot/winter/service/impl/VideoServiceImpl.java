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
package info.bluefoot.winter.service.impl;

import java.util.List;

import info.bluefoot.winter.dao.VideoDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.service.VideoService;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    
    private static Logger logger = LoggerFactory.getLogger(VideoService.class);
    
    @Inject
    private VideoDao videoDao;
    
    @Override
    public void updateLastPlayedTime(int videoId, Integer lastPlayedTime) {
        if(logger.isDebugEnabled()) {
            logger.debug("Setting last played time of video " + videoId + " to be of " + lastPlayedTime + "s");
        }
        Video v = new Video();
        v.setVideoId(videoId);
        v.setCurrentPosition(lastPlayedTime);
        videoDao.updateNotNullValues(v);
    }
    
    @Override
    public void addVideo(Video video) {
        if(logger.isDebugEnabled()) {
            logger.debug("Creating video " + video.getUrl() + " of playlist " + video.getPlaylist().getName());
        }
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
