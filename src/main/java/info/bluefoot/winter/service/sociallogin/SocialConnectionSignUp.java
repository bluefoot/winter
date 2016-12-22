package info.bluefoot.winter.service.sociallogin;

import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import info.bluefoot.winter.controller.Utils;
import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.model.SocialUser;

public class SocialConnectionSignUp implements ConnectionSignUp {

    private UserDao userDao;
    
    public SocialConnectionSignUp(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public String execute(Connection<?> connection) {
        String email = "";
        UserProfile profile = null;
        if (connection.getApi() instanceof org.springframework.social.twitter.api.impl.TwitterTemplate) {
            email = connection.getDisplayName().replace("@", "") + "@twitter.com";
        } else {
            profile = connection.fetchUserProfile();
            email = profile.getEmail();
        }
        // ========================================
        // Name verification added for facebook 1.x api, see http://stackoverflow.com/a/23986618
        // Not needed anymore since I'm using v 2 pre release, so this can be removed
        String name = connection.getDisplayName();
        if(StringUtils.isEmpty(name) || "null".equals(name)) {
            if(profile==null) {
                profile = connection.fetchUserProfile();
            }
            name = profile.getName();
        }
        if(StringUtils.isEmpty(name)) {
            name = email;
        }
        String profileUrl = connection.getProfileUrl();
        if(StringUtils.isEmpty(profileUrl)) {
            throw new RuntimeException("Error creating your user: no profile URL provided");
        }
        // ========================================
        SocialUser user = new SocialUser(profileUrl,
                email, name, Utils.getDefaultUserAuthorities());
        userDao.insertUser(user);
        userDao.insertAuthorities(user);
        return connection.getProfileUrl();
    }
}
