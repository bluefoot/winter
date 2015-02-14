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
import info.bluefoot.winter.model.SocialUser;
import info.bluefoot.winter.service.PlaylistService;
import info.bluefoot.winter.service.VideoService;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleAdapter;
import org.springframework.social.google.connect.GoogleServiceProvider;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * compress
 * implement save video priority
 *  send a "page loaded " date argument
 *  if old page sends a lesser page loaded, ignore it
 *  now, where to store it?
 *  also, what if first page closes it? dunno, maybe still ignore it
 * better break css
 * fix google scopr all you need is name and email and profile url
 * better way of showing selected playlist - see inspiration on battle.net - actually the right-border is better 
 * ERROR PAGES DEFAULT - GENERATE PROTOCOL
 * FAILURE MESSAGE LOGIN INCORRECT
 * MOVE STATIC JS TO EXTERNAL FILES, SUCH AS IN ADD-PLAYLIST.JSP
 * MAVEN COMPRESS JS AND CSS RELEASE
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
    public String login(Device device) {
//        Device device = DeviceUtils.getCurrentDevice(request);
        if (device.isMobile()) {
            System.out.println("Hello mobile user!");
        } else if (device.isTablet()) {
            System.out.println("Hello tablet user!");
        } else {
            System.out.println("Hello desktop user!");         
        }
        return "login";
    }
    @RequestMapping(value = { "/faq" }, method = RequestMethod.GET)
    public String faq() {
        return "faq";
    }
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String getAllPlaylists(Model model, Device device) {
//        Device device = DeviceUtils.getCurrentDevice(request);
        if (device.isMobile()) {
            System.out.println("Hello mobile user!");
        } else if (device.isTablet()) {
            System.out.println("Hello tablet user!");
        } else {
            System.out.println("Hello desktop user!");         
        }
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
    
    // TODO NOT WORKING BECAUSE OF AUTHENTICATION TOKEN
    @RequestMapping(value = { "/debug1234" }, method = RequestMethod.GET)
    public String forceAuthenticationForDebug() {
        SocialUser u = new SocialUser("https://plus.google.com/111200739196604053879", "gjhames@gmail.com", "Gewton Jhames", Utils.getDefaultUserAuthorities());
        u.setProviderId("google");
        u.setRank(1);
        u.setProfileUrl("https://plus.google.com/111200739196604053879");
        u.setAccessToken("ya29.GQGiI5E8FWasN0_A1w2OgOIpSb58GwrWbjh4jtEH1alaX57UFLUuu6sGPW1l1oZoyxeMwudhcaEB3A");
        u.setSecret(null);
        u.setRefreshToken(null);
        u.setExpireTime(1423854298687l);
        
        Connection<Google> connection = new OAuth2Connection<Google>("google", "https://plus.google.com/111200739196604053879", "ya29.GQGiI5E8FWasN0_A1w2OgOIpSb58GwrWbjh4jtEH1alaX57UFLUuu6sGPW1l1oZoyxeMwudhcaEB3A", null, 1423854298687l, new GoogleServiceProvider("bal", "ble"), new GoogleAdapter());
        
        SecurityContextHolder.getContext().setAuthentication(new SocialAuthenticationToken(connection, u, Collections.EMPTY_MAP, u.getAuthorities()));
        return "redirect:/";
    }

}
