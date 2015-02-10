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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * add debug log and info log on everything
 * better break css
 * MOBILE
 * REMEMBER ME NOT SAVING COOKIE
 * better way of showing selected playlist - see inspiration on battle.net - actually the right-border is better 
 * TWITCH FULL SCREEN AND YOUTUBE - WELL YOUTUBE WONT WORK FUCK
 * ERROR PAGES DEFAULT - GENERATE PROTOCOL
 * FAILURE MESSAGE LOGIN INCORRECT
 * REMOVER SOME USELESS INFO LOGS
 * MOVE STATIC JS TO EXTERNAL FILES, SUCH AS IN ADD-PLAYLIST.JSP
 * MAVEN COMPRESS JS AND CSS RELEASE
 * FAQ PAGE - NEED SCROLL OR NOT? CHECK HOW IT GOES ON WINDOWS
 * USER TRIES TO SEE A PLAYLIST THAT DOESN'T BELONG TO HIM
 * WHEN OPENING PLAYLIST, SCROLL TO IT. DANG THIS IS GONNA BE DIFFICULT
 * BUG: IS PLAYING VIDEO EVEN IF LESS THAN 5 SECONDS IF VIDEO FINISHED PLAYER WITHOUT RELOADING PAGE
 *  future:       //document.getElementById('plthumb-${selectedPlaylist.playlistId }').scrollIntoView(true);
 *  future: rename
 *  future: sort by creation date or alphabetically
 *  UserNotFoundException FIXME, LET SPRING THROW, MOVE CUSTOM EXCEPTIONS TO THE SERVICE LAYER
 * */

//http://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/
//http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-servlet
//https://github.com/spring-projects/spring-mvc-showcase/blob/master/src/main/webapp/WEB-INF/spring/root-context.xml
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
//http://www.cssreset.com/how-to-keep-footer-at-bottom-of-page-with-css/
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
    @RequestMapping(value = { "/faq" }, method = RequestMethod.GET)
    public String faq() {
        return "faq";
    }
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String getAllPlaylists(Model model) {
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
