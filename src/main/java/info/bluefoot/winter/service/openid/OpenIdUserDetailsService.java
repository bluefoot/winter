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
package info.bluefoot.winter.service.openid;

import info.bluefoot.winter.controller.Utils;
import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.dao.UserNotFoundException;
import info.bluefoot.winter.model.OpenIdUser;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OpenIdUserDetailsService implements UserDetailsService {
    
    private static Logger logger = LoggerFactory.getLogger(OpenIdUserDetailsService.class);

    @Inject
    private UserDao userDao;
    
    @Override
    public UserDetails loadUserByUsername(String openId) {
        if(logger.isDebugEnabled()) {
            logger.debug("Searching for registered OpenID " + openId);
        }
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
