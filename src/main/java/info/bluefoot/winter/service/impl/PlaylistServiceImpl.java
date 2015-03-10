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

import info.bluefoot.winter.controller.Utils;
import info.bluefoot.winter.dao.PlaylistDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.model.User;
import info.bluefoot.winter.service.PlaylistService;
import info.bluefoot.winter.service.VideoService;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    
    private static Logger logger = LoggerFactory.getLogger(PlaylistServiceImpl.class);
    
    @Inject
    private PlaylistDao playlistDao;
    
    @Inject
    private VideoService videoService;
    
    @Override
    public List<Playlist> getPlaylistsFromUser(User user) {
        List<Playlist> playlists = playlistDao.getPlaylistsByUser(user);
        for (Playlist playlist : playlists) {
            playlist.setUser(user);
        }
        if(logger.isDebugEnabled()) {
            logger.debug("Found " + playlists.size() + " playlists for user " + Utils.getCurrentLoggedUser().getUsername() + ": " + playlists);
        }
        return playlists;
    }

    @Override
    public void updateLastPlayedVideo(Integer playlistId, Integer videoId) {
        if(logger.isDebugEnabled()) {
            logger.debug("Saving last played video of playlist" + playlistId + " to be video " + videoId);
        }
        playlistDao.updateLastPlayedVideo(playlistId, videoId);
    }

    @Override
    public void addPlaylistAndVideos(Playlist playlist) {
        if(logger.isDebugEnabled()) {
            logger.debug("Creating playlist " + playlist.getName() + " containing " + playlist.getVideos().size() + " videos");
        }
        playlistDao.addPlaylist(playlist);
        for (Video video : playlist.getVideos()) {
            videoService.addVideo(video);
        }
    }

    @Override
    public void removePlaylistAndVideos(Integer playlistId) {
        if(logger.isDebugEnabled()) {
            logger.debug("Deleting playlist " + playlistId);
        }
        videoService.removeVideosFromPlaylist(playlistId);
        playlistDao.removePlaylist(playlistId);
    }

    @Override
    public Playlist getPlaylistByIdAndUser(Integer playlistId, User principal) {
        try {
            return playlistDao.getPlaylistsByIdAndUser(playlistId, principal.getUsername());
        } catch (EmptyResultDataAccessException e) {
            throw new PlaylistNotFoundException(playlistId.toString(), e);
        }
    }

    @Override
    public Playlist getPlaylistById(Integer playlistId) {
        return playlistDao.getPlaylistsById(playlistId);
    }

    @Override
    public void addVideosToPlaylist(Playlist playlist, Set<Video> videos) {
        List<Video> currentPlaylistVideos = videoService.getVideosFromPlaylistSortedByPosition(playlist);
        int position = currentPlaylistVideos.get(currentPlaylistVideos.size()-1).getSortPosition();
        for (Video video : videos) {
            video.setSortPosition(++position);
            video.setPlaylist(playlist);
            videoService.addVideo(video);
        }
    }

}
