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
package info.bluefoot.winter.config;

import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.service.sociallogin.SocialConnectionSignUp;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Value("#{systemEnvironment['WINTER_TWITTER_KEY']}")
    private String twitterKey;

    @Value("#{systemEnvironment['WINTER_TWITTER_SECRET']}")
    private String twitterSecret;
    
    @Value("#{systemEnvironment['WINTER_FACEBOOK_KEY']}")
    private String facebookKey;
    
    @Value("#{systemEnvironment['WINTER_FACEBOOK_SECRET']}")
    private String facebookSecret;
    
    @Value("#{systemEnvironment['WINTER_GOOGLE_KEY']}")
    private String googleKey;
    
    @Value("#{systemEnvironment['WINTER_GOOGLE_SECRET']}")
    private String googleSecret;
    
    @Value("#{systemEnvironment['WINTER_ENABLE_IMPLICIT_SIGNUP']?:true}")
    private boolean enableImplicitSignup;
    
    @Inject
    private DataSource dataSource;
    
    @Inject
    private UserDao userDao;
    
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        if(StringUtils.isBlank(twitterKey) || StringUtils.isBlank(facebookKey) || StringUtils.isBlank(googleKey)) {
            throw new IllegalStateException("Can't find out social login provider keys based on environment variables");
        }
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(twitterKey, twitterSecret));
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(facebookKey, facebookSecret));
        cfConfig.addConnectionFactory(new GoogleConnectionFactory(googleKey, googleSecret));
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
    
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository rep = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        if(enableImplicitSignup) {
            // Enabling implicit signup http://docs.spring.io/spring-social/docs/1.1.4.RELEASE/reference/htmlsingle/#implicit-sign-up
            rep.setConnectionSignUp(new SocialConnectionSignUp(userDao));
        }
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
        Connection<Google> connection = repository.findPrimaryConnection(Google.class);
        return connection != null ? connection.getApi() : null;
    }

}
