/*
 * Copyright 2014 the original author or authors.
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
package info.bluefoot.winter.config;

import info.bluefoot.winter.controller.Utils;
import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.model.SocialUser;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
//import org.springframework.social.facebook.web.DisconnectController;

@Configuration
@EnableSocial
public class SocialContext extends SocialConfigurerAdapter {

	@Inject
	private DataSource dataSource;
	
	@Inject
	private UserDao userDao;
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
	    cfConfig.addConnectionFactory(new TwitterConnectionFactory("RCtIDv8oe5W5hqAda3Py5Zr4e", "C42BnY6mLNElOAEHFhueD2Vy1ppA18Z36a4YGMziYA9sDYFJWO"));
	    cfConfig.addConnectionFactory(new FacebookConnectionFactory("446027038882898", "7419ee53166f54c5c8c83ccca6c63546"));
	    cfConfig.addConnectionFactory(new GoogleConnectionFactory("421555105306-1ef8cfjroj9gggm5tgiaim0smv5mv6rc.apps.googleusercontent.com", "WJ_MDFaKthzj91ZSl8cAQsys"));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {			
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return authentication.getName();
			}
		};
	}
	
	public static void main(String[] args) {
        System.out.println(StringUtils.isBlank("null"));
    }
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository rep = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		rep.setConnectionSignUp(new ConnectionSignUp() {
            
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
                // Not needed anymore since I'm using v 2 pre release
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
                userDao.insertUser(user);
                userDao.insertAuthorities(user);
                return connection.getProfileUrl();
            }
        });
        return rep;
	}

	@Bean
	public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
		return new ReconnectFilter(usersConnectionRepository, userIdSource);
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository) {
		Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Twitter twitter(ConnectionRepository repository) {
		Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
		return connection != null ? connection.getApi() : null;
	}
	
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Google google(ConnectionRepository repository) {
        System.out.println("called google? " + repository);
        Connection<Google> connection = repository.findPrimaryConnection(Google.class);
        return connection != null ? connection.getApi() : null;
    }

}
