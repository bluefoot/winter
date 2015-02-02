package info.bluefoot.watchlater.spring;

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
            userService.createNewOpenIdUser(openId, email, name);
            SecurityContextHolder.getContext().setAuthentication(((OpenIDAuthenticationToken) exception.getAuthentication()));
            // this redirects to "/" but what actually happens is that the
            // user gets redirected to the page he tried to access, which is
            // good but I don't know how it happens
            redirectStrategy.sendRedirect(request, response, "/");
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
