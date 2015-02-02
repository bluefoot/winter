package info.bluefoot.watchlater.dao.jdbc;

import info.bluefoot.watchlater.dao.UserDao;
import info.bluefoot.watchlater.model.WatchLaterUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao extends JdbcDaoSupport implements UserDao {
    @Override
    public WatchLaterUser getUserByOpenId(String userName) {
        String sql = "SELECT USER_ID, USERNAME, PASSWORD, FULL_NAME, EMAIL, OPENID, IS_OPENID_USER FROM USERS WHERE OPENID=?";
        try {
            return this.getJdbcTemplate().queryForObject(sql,
                    new Object[] { userName }, new WatchLaterUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFound("User not found: " + userName, e);
        }
    }

    @Override
    public int insertUser(final WatchLaterUser u) {
        final String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FULL_NAME, EMAIL, OPENID, IS_OPENID_USER) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, u.getUsername());
                        ps.setString(2, u.getPassword());
                        ps.setString(3, u.getFullName());
                        ps.setString(4, u.getEmail());
                        ps.setString(5, u.getOpenId());
                        ps.setBoolean(6, u.isOpenIdUser());
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }


    @Override
    public void insertAuthorities(final WatchLaterUser u) {
        final String sql = "INSERT INTO authorities (USER_ID, AUTHORITY) VALUES (?, ?)";
        for (GrantedAuthority authority : u.getAuthorities()) {
            this.getJdbcTemplate().update(sql,
                    new Object[] { u.getUserId(), authority.getAuthority()});
        }
    }

    @Override
    public List<WatchLaterUser> getAll() {
        String sql = "SELECT USER_ID, USERNAME, PASSWORD, FULL_NAME, EMAIL, OPENID, IS_OPENID_USER FROM USERS";
        return this.getJdbcTemplate().query(sql, new WatchLaterUserRowMapper());
    }
    
    class WatchLaterUserRowMapper implements RowMapper<WatchLaterUser> {

        @Override
        public WatchLaterUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            int userId = rs.getInt("USER_ID");
            String authoritiesSql = "SELECT AUTHORITY FROM AUTHORITIES WHERE USER_ID=?";
            List<SimpleGrantedAuthority> authorities = JdbcUserDao.this.getJdbcTemplate().query(authoritiesSql,
                    new Object[] { userId }, new RowMapper<SimpleGrantedAuthority>() {
                                @Override
                                public SimpleGrantedAuthority mapRow(ResultSet resultSet, int arg1)
                                        throws SQLException {
                                    return new SimpleGrantedAuthority(resultSet.getString("AUTHORITY"));
                                }
                            });
            return new WatchLaterUser(userId, 
                    rs.getString("USERNAME"), 
                    rs.getString("PASSWORD"), 
                    new HashSet<>(authorities), 
                    rs.getString("OPENID"), 
                    rs.getString("EMAIL"), 
                    rs.getString("FULL_NAME"), 
                    rs.getBoolean("IS_OPENID_USER"));
        }
        
    }
}
