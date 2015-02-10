package info.bluefoot.winter.spring;

import info.bluefoot.winter.dao.UserDao;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class WinterSocialUsersDetailService implements SocialUserDetailsService {

    @Inject
    private UserDao userDao;
    
    public WinterSocialUsersDetailService(UserDao userDao) {
        this.userDao = userDao;
    }
    
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
	    return userDao.loadSocialUser(userId);
	}

}
