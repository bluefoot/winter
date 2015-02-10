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
package info.bluefoot.winter.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Currently, Winter's only form of registration and authentication is via
 * OpenID. User is automatically registered (inserted into User's table) in the
 * first successful authentication attempt. <br />
 * But Username + Password is also supported by this class. A User can
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
public abstract class User implements UserDetails {
    
    public static final String DEFAULT_PASSWORD = "na";
    
    private static final long serialVersionUID = -8980780791782090043L;
    private final String username;
    private final String password;
    private final String email;
    private String displayName;
    private final Set<GrantedAuthority> authorities;
    
    public User(String username, String password, String email, String displayName,
            Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            throw new IllegalArgumentException("Cannot have user with empty authorities");            
        }
        if(StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Cannot have user with empty username");
        }
        if(StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException("Cannot have user with empty email");
        }
        if(StringUtils.isEmpty(password)) {
            password = DEFAULT_PASSWORD;
        }
        this.username = username;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
        this.authorities = prepareAuthorities(authorities);
    }

    public User(String username, String email, String displayName,
            Collection<? extends GrantedAuthority> authorities) {
        this(username, DEFAULT_PASSWORD, email, displayName, authorities);
    }

    protected Set<GrantedAuthority> prepareAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> returnedAuthorities = new HashSet<GrantedAuthority>();
        for (GrantedAuthority a : authorities) {
            returnedAuthorities.add(a);
        }
        return returnedAuthorities;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
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
        User other = (User) obj;
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
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Email: ").append(this.email).append("; ");
        sb.append("DisplayName: ").append(this.displayName).append("; ");

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
