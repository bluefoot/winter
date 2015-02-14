package info.bluefoot.winter.service.sociallogin;

import info.bluefoot.winter.dao.UserDao;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class WinterUserDetailsService implements UserDetailsService {

    @Inject
    private UserDao userDao;
    
    public WinterUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.loadSocialUser(username);
    }

}
