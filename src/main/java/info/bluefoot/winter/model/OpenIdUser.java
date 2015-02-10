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
