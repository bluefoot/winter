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
package info.bluefoot.winter.service.sociallogin;

import info.bluefoot.winter.dao.UserDao;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class WinterSocialUsersDetailService implements SocialUserDetailsService {
    
    private static Logger logger = LoggerFactory.getLogger(WinterSocialUsersDetailService.class);

    @Inject
    private UserDao userDao;
    
    public WinterSocialUsersDetailService(UserDao userDao) {
        this.userDao = userDao;
    }
    
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        if(logger.isDebugEnabled()) {
            logger.debug("Searching for registered social user: " + userId);
        }
	    return userDao.loadSocialUser(userId);
	}

}
