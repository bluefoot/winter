package info.bluefoot.winter.dao;

import info.bluefoot.winter.model.OpenIdUser;
import info.bluefoot.winter.model.SocialUser;
import info.bluefoot.winter.model.User;

public interface UserDao {
    User getUserByOpenId(String openid);

    void insertOpenIdUser(OpenIdUser user);
    
    void insertUser(User user);

    void insertAuthorities(User user);

    SocialUser loadSocialUser(String userId);
}
