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
package info.bluefoot.winter.controller;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.model.User;
import info.bluefoot.winter.service.PlaylistService;
import info.bluefoot.winter.service.VideoService;
import info.bluefoot.winter.service.impl.PlaylistNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//http://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/
//https://github.com/spring-projects/spring-mvc-showcase/blob/master/src/main/webapp/WEB-INF/spring/root-context.xml
//http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-servlet
//http://discuss.dev.twitch.tv/t/scrub-to-time-on-embedded-player/120
//https://github.com/justintv/Twitch-API
//http://tympanus.net/Development/CreativeLinkEffects/
//https://github.com/justintv/Twitch-API/blob/master/player.md
//http://discuss.dev.twitch.tv/t/embedding-player-with-javascript-api-callback/1198/5
//http://discuss.dev.twitch.tv/t/embedding-player-with-javascript-api-callback/1198/4
//https://developers.google.com/youtube/v3/docs/videos#fileDetails
//https://developers.google.com/youtube/player_parameters?playerVersion=HTML5#start
//http://diveintohtml5.info/history.html
//http://dimsemenov.com/plugins/magnific-popup/documentation.html
//http://astronautweb.co/snippet/font-awesome/
@Controller
public class PlaylistController {

    @Inject
    private PlaylistService playlistService;

    @Inject
    private VideoService videoService;
    
    private static Logger logger = LoggerFactory.getLogger(PlaylistController.class);

    // @PreAuthorize("hasPermission(#playlist, 'owner')")
    @RequestMapping(value = { "/playlist/{id}" }, method = RequestMethod.GET)
    public String play(@PathVariable Integer id, Model model, Device device) {
        try {
            Playlist selectedPlaylist = playlistService.getPlaylistByIdAndUser(
                    id, 
                    Utils.getCurrentLoggedUser());
//            Playlist selectedPlaylist = playlistService.getPlaylistById(id);
            List<Video> videos = videoService.getVideosFromPlaylist(selectedPlaylist);
            model.addAttribute("selectedPlaylist", selectedPlaylist);
            model.addAttribute("videos", videos);
            if (device.isMobile()) {
                return "playlist";
            } else {
                List<Playlist> playlists = playlistService.getPlaylistsFromUser(Utils.getCurrentLoggedUser());
                model.addAttribute("playlists", playlists);
                return "home";
            }
        } catch (PlaylistNotFoundException e) {
            if(logger.isDebugEnabled()) {
                logger.debug("Playlist " + id + " not found, redirecting to home");
            }
            return "redirect:/";
        }
    }
    
    @RequestMapping(value = { "/playlist/{id}/video/{videoId}" }, method = RequestMethod.GET)
    public String play(@PathVariable Integer id, @PathVariable Integer videoId, Model model) {
        try {
            Playlist selectedPlaylist = playlistService.getPlaylistByIdAndUser(
                    id, 
                    Utils.getCurrentLoggedUser());
            List<Playlist> playlists = playlistService.getPlaylistsFromUser(Utils.getCurrentLoggedUser());
            List<Video> videos = videoService.getVideosFromPlaylist(selectedPlaylist);
            model.addAttribute("playlists", playlists);
            model.addAttribute("selectedPlaylist", selectedPlaylist);
            model.addAttribute("videos", videos);
            model.addAttribute("videoToPlay", videoId);
            return "home";
        } catch (PlaylistNotFoundException e) {
            if(logger.isDebugEnabled()) {
                logger.debug("Playlist " + id + " not found, redirecting to home");
            }
            return "redirect:/";
        }
    }
    
    @RequestMapping(value = { "/playlist/delete" }, method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<Map<String, String>> removePlaylist(
            Integer playlistId, HttpServletResponse response) {
        System.out.println("removing " + playlistId);
        try {
            playlistService.removePlaylistAndVideos(playlistId);
        } catch (Exception e) {
            String errorId = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            logger.error("[" + errorId + "] Can't delete playlist", e);
            return new ResponseEntity<Map<String, String>>(
                    Collections.singletonMap("error", "Can't delete playlist. Try again later. Error id: " + errorId + "."), 
                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Map<String, String>>(
                Collections.singletonMap("sucess", "true"),
                HttpStatus.OK);    }
    
    @RequestMapping(value = { "/playlist/add" }, method = RequestMethod.GET)
    public String addPlaylistDisplayForm(Model model) {
        model.addAttribute("addplaylistform", new AddPlaylistForm());
        return "add-playlist";
    }

    @RequestMapping(value = { "/playlist/add" }, method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<Map<String, String>> addPlaylist(
            @ModelAttribute("addplaylistform") AddPlaylistForm addPlaylistForm,
            HttpServletResponse response) {
        Set<Video> videos = new HashSet<Video>();
        int sort=0;
        for (String url : addPlaylistForm.getVideos().split("\n")) {
            if (!url.trim().isEmpty()) {
                videos.add(new Video(url.trim(), sort++).setPlaylist(addPlaylistForm.getPlaylist()));
            }
        }

        addPlaylistForm.getPlaylist().setVideos(new ArrayList<Video>(videos));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addPlaylistForm.getPlaylist().setUser(user);
        try {
            playlistService.addPlaylistAndVideos(addPlaylistForm.getPlaylist());
        } catch (Exception e) {
            String errorId = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            logger.error("[" + errorId + "] Can't create playlist", e);
            return new ResponseEntity<Map<String, String>>(
                    Collections.singletonMap("error", "Can't create playlist. Try again later. Error id: " + errorId + "."), 
                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Map<String, String>>(
                Collections.singletonMap("playlist_id", String.valueOf(addPlaylistForm.getPlaylist().getPlaylistId())),
                HttpStatus.OK);
    }
}
