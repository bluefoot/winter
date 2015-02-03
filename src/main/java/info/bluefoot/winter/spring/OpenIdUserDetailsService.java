package info.bluefoot.winter.spring;

import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.dao.exception.UserNotFoundException;
import info.bluefoot.winter.model.WinterUser;

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
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(openId, e);
        }
    }

    public void createNewOpenIdUser(String openId, String email, String fullName) {
        WinterUser user = new WinterUser(openId,
                new HashSet<SimpleGrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))),
                email, fullName);
        user.setUserId(userDao.insertUser(user));
        userDao.insertAuthorities(user);
    }
}
