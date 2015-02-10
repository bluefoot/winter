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
package info.bluefoot.winter.controller;

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.model.OpenIdUser;
import info.bluefoot.winter.model.SocialUser;
import info.bluefoot.winter.model.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SignupController {

	private final ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
	
	@Inject
	UserDao userDao;

	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signupForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message", "MSG", WebRequest.SCOPE_REQUEST);
			UserProfile userProf = connection.fetchUserProfile();
//			createTheGodsDamnAccount(userProf);
			return "redirect:/";
		} else {
			return "redirect:/";
		}
	}

    private void createTheGodsDamnAccount(UserProfile userProf) {
//        SocialUser user = new SocialUser(userProf.getUsername(), userProf.getEmail(), userProf.getName(), new HashSet<SimpleGrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//        userDao.insertOpenIdUser(wu);
    }
}
