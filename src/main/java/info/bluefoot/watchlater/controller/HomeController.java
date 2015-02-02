package info.bluefoot.watchlater.controller;

import info.bluefoot.watchlater.dao.UserDao;
import info.bluefoot.watchlater.model.Playlist;
import info.bluefoot.watchlater.model.Video;
import info.bluefoot.watchlater.model.WatchLaterUser;
import info.bluefoot.watchlater.service.PlaylistService;
import info.bluefoot.watchlater.service.VideoService;

import java.util.List;

import javax.annotation.PostConstruct;
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
 * remove .jsp of login page
 * DISABLE USER+PASSWORD LOGIN VIA SECURITY CONTEXT. STILL POSSIBLE VIA POST FORM
 * EDIT
 * REMOVE PRINTS, REFACTORY
 * MOBILE
 * better way of showing selected playlist - see inspiration on battle.net - actually the right-border is better 
 * work link for playing single video
 * postgres
 * ERROR PAGES DEFAULT - GENERATE PROTOCOL
 * FAILURE MESSAGE INSERT PLAYLIST
 * FAILURE MESSAGE LOGIN INCORRECT
 * REMEMBERME
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
 * http://blogs.isostech.com/spring-2/adding-google-authentication-spring-security-application/
 * http://java.dzone.com/articles/steps-enable-openid
 * https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/tree/master/openid-connect-client
 * https://www.packtpub.com/books/content/opening-openid-spring-security
 * http://www.petrikainulainen.net/programming/spring-framework/adding-social-sign-in-to-a-spring-mvc-web-application-configuration/
 *      http://www.petrikainulainen.net/programming/spring-framework/adding-social-sign-in-to-a-spring-mvc-web-application-registration-and-login/
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
    
    @Inject
    private UserDao userDao;
    
    @PostConstruct
    public void test() {
        System.out.println("trying all users...");
        System.out.println(userDao.getAll());
        System.out.println("sucessfully got all users");
    }
    
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String getAllPlaylists(Model model) {
        List<WatchLaterUser> users = userDao.getAll();
        for (WatchLaterUser user : users) {
            System.out.println(user);
        }
        List<Playlist> playlists=getUsersPlaylists();
        model.addAttribute("playlists", playlists);
        return "home";
    }

//    @PreAuthorize("hasPermission(#playlist, 'owner')")
    @RequestMapping(value = { "/playlist/{id}" }, method = RequestMethod.GET)
    public String play(@PathVariable Integer id, Model model) {
        List<Playlist> playlists=getUsersPlaylists();
        List<Video> videos = getVideosFromPlaylist(id);
        model.addAttribute("playlists", playlists);
        Playlist selectedPlaylist = null;
        for(Playlist p : playlists) {
            if(p.getPlaylistId()==id) {
                selectedPlaylist = p;
            }
        }
        model.addAttribute("selectedPlaylist", selectedPlaylist);
        model.addAttribute("videos", videos);
        return "home";
    }
    
     @RequestMapping(value = { "/video/savetime" }, method = RequestMethod.POST)
     @ResponseBody
    public  void saveCurrentTime(HttpServletRequest request) {
        Integer videoId = Integer.valueOf(request.getParameter("videoId"));
        Integer playlistId = Integer.valueOf(request.getParameter("playlistId"));
        Integer currentTime = Integer.valueOf(request.getParameter("currentTime"));
        videoService.updateLastPlayedTime(videoId, currentTime);
        playlistService.updateLastPlayedVideo(playlistId, videoId);
    }
    
    private List<Video> getVideosFromPlaylist(Integer id) {
        return playlistService.getVideosFromPlaylist(id);
    }

    private List<Playlist> getUsersPlaylists() {
        WatchLaterUser user = (WatchLaterUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playlistService.getUsersPlaylists(user.getUsername());
    }
}
