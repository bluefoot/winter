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

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class OpenIDAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {
    @Inject
    private OpenIdUserDetailsService userService;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception instanceof UsernameNotFoundException
                && exception.getAuthentication() instanceof OpenIDAuthenticationToken
                && ((OpenIDAuthenticationToken) exception.getAuthentication())
                        .getStatus().equals(OpenIDAuthenticationStatus.SUCCESS)) {
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            //FIXME is this "USER_OPENID_CREDENTIAL" used anywhere?
            request.getSession(true).setAttribute(
                    "USER_OPENID_CREDENTIAL",
                    ((UsernameNotFoundException) exception)
                            .getExtraInformation());
            // User successful authenticated but doesn't exists in the database
            // So this will create the new user
            String openId = (String) ((OpenIDAuthenticationToken) exception.getAuthentication()).getPrincipal();
            List<OpenIDAttribute> attributes = ((OpenIDAuthenticationToken) exception.getAuthentication()).getAttributes();
            String email = null;
            String name = "";
            for (OpenIDAttribute attr : attributes) {
                if(attr.getName().equals("email")) {
                    email = attr.getValues().get(0);
                }
                if(attr.getName().equals("firstName")) {
                    if(!StringUtils.isBlank(attr.getValues().get(0))) {
                        name = attr.getValues().get(0) + " " + name;
                    }
                }
                if(attr.getName().equals("lastName")) {
                    if(!StringUtils.isBlank(attr.getValues().get(0))) {
                        name = name + " " + attr.getValues().get(0);
                    }
                }
            }
            if(StringUtils.isBlank(name)) {
                name = email;
            }
            if(logger.isDebugEnabled()) {
                logger.debug("Successful authentication of OpenID user " + openId + ". Creating new user with email: " + email + " and name: " + name);
            }
            userService.createNewOpenIdUser(openId, email, name);
            SecurityContextHolder.getContext().setAuthentication(((OpenIDAuthenticationToken) exception.getAuthentication()));
            // this redirects to "/" but what actually happens is that the
            // user gets redirected to the page he tried to access, which is
            // good but I don't know how it happens
            redirectStrategy.sendRedirect(request, response, "/");
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("Failure logging in OpenID user with exception: " + exception.getClass(), exception);
            }
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
