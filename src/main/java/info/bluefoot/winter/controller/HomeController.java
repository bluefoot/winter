package info.bluefoot.winter.controller;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.model.WinterUser;
import info.bluefoot.winter.service.PlaylistService;
import info.bluefoot.winter.service.VideoService;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * MISSING: FIX LOGIN GMAIL
 * css login page
 * EDIT
 * MOBILE
 * REMEMBER ME NOT SAVING COOKIE
 * better way of showing selected playlist - see inspiration on battle.net - actually the right-border is better 
 * work link for playing single video
 * ERROR PAGES DEFAULT - GENERATE PROTOCOL
 * FAILURE MESSAGE INSERT PLAYLIST
 * FAILURE MESSAGE LOGIN INCORRECT
 * SPINNER WHEN ADDING VIDEO!!
 * FIX WERROR WHEN ADDING VIDEO MSG WHERE TO SHOW
 * */

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

//http://www.tutorialspoint.com/spring/spring_mvc_form_handling_example.htm
//http://www.sanwebe.com/2014/08/css-html-forms-designs
//http://www.codejava.net/frameworks/spring/spring-mvc-form-handling-tutorial-and-example
//http://examples.javacodegeeks.com/enterprise-java/spring/mvc/spring-mvc-form-handling-example/
//https://spring.io/guides/gs/handling-form-submission/
//http://www.mkyong.com/spring-mvc/spring-3-mvc-and-jsr303-valid-example/
//http://viralpatel.net/blogs/spring-mvc-multi-row-submit-java-list/
//https://code.google.com/p/openid-selector/
/**
 * openid:
 * http://blogs.isostech.com/spring-2/adding-google-authentication-spring
 * -security-application/ http://java.dzone.com/articles/steps-enable-openid
 * https
 * ://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/tree/master
 * /openid-connect-client
 * https://www.packtpub.com/books/content/opening-openid-spring-security
 * http://www
 * .petrikainulainen.net/programming/spring-framework/adding-social-sign
 * -in-to-a-spring-mvc-web-application-configuration/
 * http://www.petrikainulainen
 * .net/programming/spring-framework/adding-social-sign
 * -in-to-a-spring-mvc-web-application-registration-and-login/
 * 
 * 
 * 
 * @author bluefoot
 *
 */
@Controller
public class HomeController {

    @Inject
    private PlaylistService playlistService;

    @Inject
    private VideoService videoService;
    
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String getAllPlaylists(Model model) {
        List<Playlist> playlists = getUsersPlaylists();
        model.addAttribute("playlists", playlists);
        return "home";
    }

    // @PreAuthorize("hasPermission(#playlist, 'owner')")
    @RequestMapping(value = { "/playlist/{id}" }, method = RequestMethod.GET)
    public String play(@PathVariable Integer id, Model model) {
        List<Playlist> playlists = getUsersPlaylists();
        Playlist selectedPlaylist = null;
        for (Playlist p : playlists) {
            if (p.getPlaylistId().equals(id)) {
                selectedPlaylist = p;
            }
        }
        List<Video> videos = videoService.getVideosFromPlaylist(selectedPlaylist);
        model.addAttribute("playlists", playlists);
        model.addAttribute("selectedPlaylist", selectedPlaylist);
        model.addAttribute("videos", videos);
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

    private List<Playlist> getUsersPlaylists() {
        WinterUser user = (WinterUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playlistService.getPlaylistsFromUser(user);
    }
}
