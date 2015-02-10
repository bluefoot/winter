package info.bluefoot.winter.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class OpenIdUser extends User implements UserDetails {
    private static final long serialVersionUID = -1569237095218583350L;
    
    public OpenIdUser(String username, String email, String displayName, 
            Collection<? extends GrantedAuthority> authorities) {
        super(username, email, displayName, authorities);
    }
    
    public String getOpenId() {
        return getUsername();
    }

    @Override
    public String toString() {
        // Modified from String org.springframework.security.core.userdetails.User.toString()
        StringBuilder sb = new StringBuilder();
        sb.append("OpenIDUser").append(": ");
        sb.append(super.toString());
        return sb.toString();
    }
 
}
