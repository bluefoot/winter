package info.bluefoot.winter.spring;

import info.bluefoot.winter.controller.Utils;
import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.dao.exception.UserNotFoundException;
import info.bluefoot.winter.model.OpenIdUser;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OpenIdUserDetailsService implements UserDetailsService {

    @Inject
    private UserDao userDao;
    
    @Override
    public UserDetails loadUserByUsername(String openId) {
        try {
            return userDao.getUserByOpenId(openId);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(openId, e);
        }
    }

    public void createNewOpenIdUser(String openId, String email, String fullName) {
        OpenIdUser user = new OpenIdUser(openId, email, fullName, Utils.getDefaultUserAuthorities());
        userDao.insertOpenIdUser(user);
        userDao.insertAuthorities(user);
    }
}
