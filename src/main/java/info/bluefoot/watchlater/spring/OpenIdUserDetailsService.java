package info.bluefoot.watchlater.spring;

import info.bluefoot.watchlater.dao.UserDao;
import info.bluefoot.watchlater.dao.jdbc.UserNotFound;
import info.bluefoot.watchlater.model.WatchLaterUser;

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        } catch (UserNotFound e) {
            throw new UsernameNotFoundException(openId);
        }
    }

    public void createNewOpenIdUser(String openId, String email, String fullName) {
        WatchLaterUser user = new WatchLaterUser(openId,
                new HashSet<SimpleGrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))),
                email, fullName);
        user.setUserId(userDao.insertUser(user));
        userDao.insertAuthorities(user);
    }

}
