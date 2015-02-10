package info.bluefoot.winter.service.sociallogin;

import info.bluefoot.winter.controller.Utils;
import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.model.SocialUser;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

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
        // ========================================
        SocialUser user = new SocialUser(connection.getProfileUrl(),
                email, name, Utils.getDefaultUserAuthorities());
        try {
        userDao.insertUser(user);
        } catch (DuplicateKeyException e) {
            if(e.getCause() != null && e.getCause().getMessage() != null && e.getCause().getMessage().contains("email")) {
                throw new EmailAlreadyRegisteredException("Email already registered: " + email, e);
            }
            throw e;
        }
        userDao.insertAuthorities(user);
        return connection.getProfileUrl();
    }
}
