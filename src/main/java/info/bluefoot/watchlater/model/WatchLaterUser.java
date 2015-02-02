package info.bluefoot.watchlater.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Currently, WatchLater's only form of registration and authentication is via
 * OpenID. User is automatically registered (inserted into User's table) in the
 * first successful authentication attempt. <br />
 * But Username + Password is also supported by this class. A WatchLaterUser can
 * be an OpenID user or not. <br />
 * If is an OpenID user, property {@link #openIdUser} will be true and
 * properties {@link #username} and {@link #openId} will be equal. <br />
 * If user is not an OpenID user, property {@link #openIdUser} will be false and
 * properties {@link #openId} and {@link #password} will be null. <br />
 * This class provides three constructors: one for an OpenID user, another for a
 * non-OpenID user, and a third constructor that receives all arguments and
 * based on the <tt>openIdUser</tt> boolean parameter it identifies if it's an OpenID
 * user or not. <br />
 * Although user+password is supported here and in the database, the app doesn't
 * provide any way of registering regular users.
 * 
 * @author bluefoot
 *
 */
public class WatchLaterUser implements UserDetails {
    
    private static final long serialVersionUID = -8980780791782090043L;
    private Integer userId;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final String openId;
    private final String email;
    private final String fullName;
    private final boolean openIdUser;
    public WatchLaterUser(int userId, String username, String password,
            Collection<? extends GrantedAuthority> authorities, String openId, String email,
            String fullName, boolean openIdUser) {
        if ((authorities == null || authorities.isEmpty())
                || StringUtils.isEmpty(username) 
                || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(fullName)
                || (openIdUser && StringUtils.isEmpty(openId))
                || (!openIdUser && StringUtils.isEmpty(password))) {
            throw new IllegalArgumentException(
                    "Cannot have user with empty fields");
        }
        if(openIdUser && !username.equals(openId)) {
            throw new IllegalArgumentException("Something is wrong with user " + username + ": username doesn't match openid: " + openId);
        }
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = sortAuthorities(authorities);
        this.openId = openId;
        this.email = email;
        this.fullName = fullName;
        this.openIdUser = openIdUser;
    }

    public WatchLaterUser(String openId, Collection<? extends GrantedAuthority> authorities, 
            String email, String fullName) {
        if ((authorities == null || authorities.isEmpty())
                || StringUtils.isEmpty(openId) 
                || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(fullName)) {
            throw new IllegalArgumentException(
                    "Cannot have user with empty authorities, openid, email or full name");
        }
        this.password = null;
        this.username = openId;
        this.openIdUser = true;
        this.authorities = sortAuthorities(authorities);
        this.openId = openId;
        this.email = email;
        this.fullName = fullName;
    }

    public WatchLaterUser(String username, String password,
            Collection<? extends GrantedAuthority> authorities, String email, String fullName) {
        if ((authorities == null || authorities.isEmpty())
                || StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(fullName)) {
            throw new IllegalArgumentException(
                    "Cannot have user with empty authorities, username and password (except if openId user), email or full name");
        }
        this.openId = null;
        this.openIdUser = false;
        this.username = username;
        this.password = password;
        this.authorities = sortAuthorities(authorities);
        this.email = email;
        this.fullName = fullName;
    }
    
    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>();
        Set<GrantedAuthority> returnedAuthorities = new HashSet<GrantedAuthority>();
        for (GrantedAuthority a : authorities) {
            returnedAuthorities.add(a);
        }
        return returnedAuthorities;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isOpenIdUser() {
        return openIdUser;
    }

    public String getOpenId() {
        return openId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WatchLaterUser other = (WatchLaterUser) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        // Modified from String org.springframework.security.core.userdetails.User.toString()
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("User ID: ").append(this.userId).append("; ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("OpenID: ").append(this.openId).append("; ");
        sb.append("Email: ").append(this.email).append("; ");
        sb.append("FullName: ").append(this.fullName).append("; ");
        sb.append("OpenIdUser: ").append(this.openIdUser).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }
        return sb.toString();
    }
 
}
