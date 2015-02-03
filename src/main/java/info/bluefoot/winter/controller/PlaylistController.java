package info.bluefoot.winter.controller;

import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.model.WinterUser;
import info.bluefoot.winter.service.PlaylistService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
    private static Logger logger = LoggerFactory.getLogger(PlaylistController.class);

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
                videos.add(new Video(url.trim(), sort++));
            }
        }

        addPlaylistForm.getPlaylist().setVideos(new ArrayList<Video>(videos));
        WinterUser user = (WinterUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addPlaylistForm.getPlaylist().setUser(user);
        try {
            playlistService.addPlaylistAndVideos(addPlaylistForm.getPlaylist());
        } catch (Exception e) {
            String errorId = RandomStringUtils.randomAlphanumeric(6);
            logger.error("[" + errorId + "] Can't create playlist", e);
            return new ResponseEntity<Map<String, String>>(
                    Collections.singletonMap("error", 
                            "Can't create playlist. This error has been logged, contact us with error id " + errorId), 
                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Map<String, String>>(
                Collections.singletonMap("playlist_id", String.valueOf(addPlaylistForm.getPlaylist().getPlaylistId())),
                HttpStatus.OK);
    }
}
