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
import info.bluefoot.winter.service.sociallogin.WinterSocialUsersDetailService;
import info.bluefoot.winter.service.sociallogin.WinterUserDetailsService;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private DataSource dataSource;
	
//	@Inject
//    public UserDetailsService userDetailsService;
	
	@Inject
	private UserDao userDao;
	
	@Override
	protected UserDetailsService userDetailsService() {
	    return new WinterUserDetailsService(userDao);
//	    return userDetailsService;
	}
	
	@Bean
	public RememberMeServices rememberMeServices() {
	    TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("winterapp23823829389", userDetailsService());
	    rememberMeServices.setAlwaysRemember(true);
	    rememberMeServices.setTokenValiditySeconds(15552000); // 6 months
	    rememberMeServices.setCookieName("winter-rmm-key");
	    return rememberMeServices;
	}
	
	//FIXME url rewriting how to disable
    //FIXME remember me parameter: new org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices().setp
	//isAuthenticated() and hasRole('ROLE_USER')
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService())
            .rememberMe()
                .rememberMeServices(rememberMeServices())
            .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .deleteCookies("JSESSIONID")
            .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?fail")
            .and()
                .authorizeRequests()
                    .antMatchers(
                            "/login", 
                            "/faq", 
                            "/resources/**",
                            "/auth/**", 
                            "/signin/**", 
                            "/signup/**",
                            "/debug1234", //for debugging
                            "/disconnect/facebook")
                                .permitAll()
                    .antMatchers(
                            "/**")
                                .authenticated()
            .and()
                .csrf().disable()
            .apply(new SpringSocialConfigurer());
    }
    
    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
        super.configure(auth);
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/resources/**");
	}
	
	@Bean
	@Inject
	public SocialUserDetailsService socialUserDetailsService(UserDao userDao) {
	    return new WinterSocialUsersDetailService(userDao);
	}
	
	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

}
