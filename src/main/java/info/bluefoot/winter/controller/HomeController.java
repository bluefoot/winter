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
import info.bluefoot.winter.service.PlaylistService;
import info.bluefoot.winter.service.VideoService;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Home controller for the application
 */
@Controller
public class HomeController {
    
    @Inject
    private PlaylistService playlistService;

    @Inject
    private VideoService videoService;

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(Device device) {
//        Device device = DeviceUtils.getCurrentDevice(request);
        return "login";
    }
    @RequestMapping(value = { "/faq" }, method = RequestMethod.GET)
    public String faq() {
        return "faq";
    }
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String getAllPlaylists(Model model, Device device) {
        List<Playlist> playlists = playlistService.getPlaylistsFromUser(Utils.getCurrentLoggedUser());
        model.addAttribute("playlists", playlists);
        return "home";
    }

    @RequestMapping(value = { "/video/savetime" }, method = RequestMethod.POST)
    @ResponseBody
    public void saveCurrentTime(HttpServletRequest request) {
        Integer videoId = Integer.valueOf(request.getParameter("videoId"));
        Integer playlistId = Integer.valueOf(request.getParameter("playlistId"));
        Integer currentTime = Integer.valueOf(request.getParameter("currentTime"));
        videoService.updateLastPlayedTime(videoId, currentTime);
        playlistService.updateLastPlayedVideo(playlistId, videoId);
    }

}
