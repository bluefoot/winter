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
import org.springframework.social.security.SocialUserDetails;

public class SocialUser extends User implements SocialUserDetails {

    private static final long serialVersionUID = -2045481815977252334L;
    
    private String providerId;
    private String providerUserId;
    private Integer rank;
    private String profileUrl;
    private String imageUrl;
    private String accessToken;
    private String secret;
    private String refreshToken;
    private Long expireTime;
    
    public SocialUser(String username, String email, String displayName,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, email, displayName, authorities);
    }

    public String getUserId() {
        return getUsername();
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
    
    
    @Override
    public String toString() {
        // Modified from String org.springframework.security.core.userdetails.User.toString()
        StringBuilder sb = new StringBuilder();
        sb.append("SocialUser").append(": ");
        sb.append("providerId: ").append(this.providerId).append("; ");
        sb.append("providerUserId: ").append(this.providerUserId).append("; ");
        sb.append("rank: ").append(this.rank).append("; ");
        sb.append("profileUrl: ").append(this.profileUrl).append("; ");
        sb.append("imageUrl: ").append(this.imageUrl).append("; ");
        sb.append("accessToken: ").append(this.accessToken).append("; ");
        sb.append("secret: ").append(this.secret).append("; ");
        sb.append("refreshToken: ").append(this.refreshToken).append("; ");
        sb.append("expireTime: ").append(this.expireTime).append("; ");
        sb.append(super.toString());
        return sb.toString();
    }
}
