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

import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.spring.WinterSocialUsersDetailService;

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
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableGlobalAuthentication
public class SecurityContext extends WebSecurityConfigurerAdapter{

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private DataSource dataSource;
	
	@Inject
    public UserDetailsService userDetailsService;
	
	@Override
	protected UserDetailsService userDetailsService() {
	    return userDetailsService;
	}
	
	@Bean
	public UserDetailsService openIdUserService() {
	    return new info.bluefoot.winter.spring.OpenIdUserDetailsService();
	}
	
	@Bean
	public AuthenticationUserDetailsService<OpenIDAuthenticationToken> authenticationUserDetailsService() {
	    return new UserDetailsByNameServiceWrapper<OpenIDAuthenticationToken>(openIdUserService());
	}
	
	@Bean
	public AuthenticationFailureHandler openIdAuthFailureHandler() {
	    info.bluefoot.winter.spring.OpenIDAuthenticationFailureHandler handler = new info.bluefoot.winter.spring.OpenIDAuthenticationFailureHandler();
	    handler.setDefaultFailureUrl("/login?fail");
	    return handler;
	}
	
	//FIXME url rewriting how to disable
    //FIXME remember me parameter: new org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices().setp
	//isAuthenticated() and hasRole('ROLE_USER')
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService())
            .rememberMe()
                .tokenValiditySeconds(7776000)
                .key("winterapp23823829389")
                .userDetailsService(openIdUserService())
            .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .deleteCookies("JSESSIONID")
            .and()
                .openidLogin()
                    .defaultSuccessUrl("/")
                    .authenticationUserDetailsService(authenticationUserDetailsService())
                    .failureHandler(openIdAuthFailureHandler())
                    .loginProcessingUrl("/openidlogin")
                    .loginPage("/login")
                    .attributeExchange(".*")
                        .attribute(new OpenIDAttribute("email", "http://axschema.org/contact/email"))
                        .attribute(new OpenIDAttribute("firstName", "http://axschema.org/namePerson/first"))
                        .attribute(new OpenIDAttribute("lastName", "http://axschema.org/namePerson/last"))
                    .and()
            .and()    
                .authorizeRequests()
                    .antMatchers(
                            "/login", 
                            "/faq", 
                            "/resources/**",
                            "/auth/**", 
                            "/signin/**", 
                            "/signup/**",
                            "/disconnect/facebook")
                                .permitAll()
                    .antMatchers(
                            "/**")
                                .authenticated()
            .and()
                .apply(new SpringSocialConfigurer());
    }
    
    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService());
        super.configure(auth);
    }
	
	///////////////////////////////////
	
//	@Autowired
//	public void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication()
//				.dataSource(dataSource)
//				.usersByUsernameQuery("select username, password, true from Account where username = ?")
//				.authoritiesByUsernameQuery("select username, 'ROLE_USER' from Account where username = ?")
//				.passwordEncoder(passwordEncoder());
//	}
	
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
