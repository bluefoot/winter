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
package info.bluefoot.winter.dao.jdbc;

import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.dao.UserNotFoundException;
import info.bluefoot.winter.model.OpenIdUser;
import info.bluefoot.winter.model.SocialUser;
import info.bluefoot.winter.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao extends JdbcDaoSupport implements UserDao {
    
    @Inject 
    public JdbcUserDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }
    
    @Override
    public User getUserByOpenId(String userName) {
        String sql = "SELECT U.USERNAME, U.PASSWORD, U.EMAIL, UOID.DISPLAYNAME FROM USERS U INNER JOIN USEROPENIDCONNECTION UOID ON (U.USERNAME=UOID.OPENID) WHERE UOID.OPENID=?";
        try {
            return this.getJdbcTemplate().queryForObject(sql,
                    new Object[] { userName }, new OpenIdUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Open ID User not found: " + userName, e);
        }
    }

    @Override
    public void insertOpenIdUser(final OpenIdUser user) {
        String userSql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL) VALUES (?, ?, ?)";
        this.getJdbcTemplate().update(userSql, new Object[] {user.getUsername(), user.getPassword(), user.getEmail()});
        String openIduserSql = "INSERT INTO USEROPENIDCONNECTION (OPENID, DISPLAYNAME) VALUES (?, ?)";
        this.getJdbcTemplate().update(openIduserSql, new Object[] {user.getOpenId(), user.getDisplayName()});
    }
    

    @Override
    public void insertUser(User user) {
        String userSql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL) VALUES (?, ?, ?)";
        this.getJdbcTemplate().update(userSql, new Object[] {user.getUsername(), user.getPassword(), user.getEmail()});
    }


    @Override
    public void insertAuthorities(final User user) {
        final String sql = "INSERT INTO AUTHORITY (USERNAME, AUTHORITY) VALUES (?, ?)";
        for (GrantedAuthority authority : user.getAuthorities()) {
            this.getJdbcTemplate().update(sql,
                    new Object[] { user.getUsername(), authority.getAuthority()});
        }
    }
    
    class OpenIdUserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            String username = rs.getString("USERNAME");
            String authoritiesSql = "SELECT AUTHORITY FROM AUTHORITY WHERE USERNAME=?";
            List<SimpleGrantedAuthority> authorities = JdbcUserDao.this.getJdbcTemplate().query(authoritiesSql,
                    new Object[] { username }, new RowMapper<SimpleGrantedAuthority>() {
                                @Override
                                public SimpleGrantedAuthority mapRow(ResultSet resultSet, int rowNum)
                                        throws SQLException {
                                    return new SimpleGrantedAuthority(resultSet.getString("AUTHORITY"));
                                }
                            });
            return new OpenIdUser(username, rs.getString("EMAIL"), rs.getString("DISPLAYNAME"), authorities);
        }
    }

    @Override
    public SocialUser loadSocialUser(String userId) {
        String sql = "SELECT USERID, PROVIDERID, PROVIDERUSERID, RANK, " + 
                     "DISPLAYNAME, PROFILEURL, IMAGEURL, ACCESSTOKEN, " +
                     "SECRET, REFRESHTOKEN, EXPIRETIME FROM USERCONNECTION WHERE USERID=?";
        return this.getJdbcTemplate().queryForObject(sql, new Object[] {userId}, new RowMapper<SocialUser>() {
            @Override
            public SocialUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                String username = rs.getString("userid");
                String authoritiesSql = "SELECT AUTHORITY FROM AUTHORITY WHERE USERNAME=?";
                List<SimpleGrantedAuthority> authorities = JdbcUserDao.this.getJdbcTemplate().query(authoritiesSql,
                        new Object[] { username }, new RowMapper<SimpleGrantedAuthority>() {
                                    @Override
                                    public SimpleGrantedAuthority mapRow(ResultSet resultSet, int rowNum)
                                            throws SQLException {
                                        return new SimpleGrantedAuthority(resultSet.getString("AUTHORITY"));
                                    }
                                });
                String userSql = "SELECT EMAIL FROM USERS WHERE USERNAME=?";
                String email = JdbcUserDao.this.getJdbcTemplate().queryForObject(userSql, new Object[] { username }, new RowMapper<String>() {

                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("EMAIL");
                    }
                    
                });
                SocialUser u = new SocialUser(username, email, rs.getString("DISPLAYNAME"), authorities);
                u.setProviderId(rs.getString("providerid"));
                u.setRank(rs.getInt("rank"));
                u.setProfileUrl(rs.getString("profileurl"));
                u.setAccessToken(rs.getString("accesstoken"));
                u.setSecret(rs.getString("secret"));
                u.setRefreshToken(rs.getString("refreshtoken"));
                u.setExpireTime(rs.getLong("expiretime"));
                return u;
            }
        });
    }

}
