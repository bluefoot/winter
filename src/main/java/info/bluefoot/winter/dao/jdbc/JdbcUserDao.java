package info.bluefoot.winter.dao.jdbc;

import info.bluefoot.winter.dao.UserDao;
import info.bluefoot.winter.dao.exception.UserNotFoundException;
import info.bluefoot.winter.model.WinterUser;

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
    public WinterUser getUserByOpenId(String userName) {
        String sql = "SELECT USER_ID, USERNAME, PASSWORD, FULL_NAME, EMAIL, OPENID, IS_OPENID_USER FROM USERS WHERE OPENID=?";
        try {
            return this.getJdbcTemplate().queryForObject(sql,
                    new Object[] { userName }, new WinterUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User not found: " + userName, e);
        }
    }

    @Override
    public int insertUser(final WinterUser user) {
        final String sql = "INSERT INTO USERS (USERNAME, PASSWORD, FULL_NAME, EMAIL, OPENID, IS_OPENID_USER) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, user.getUsername());
                        ps.setString(2, user.getPassword());
                        ps.setString(3, user.getFullName());
                        ps.setString(4, user.getEmail());
                        ps.setString(5, user.getOpenId());
                        ps.setBoolean(6, user.isOpenIdUser());
                        return ps;
                    }
                },
                keyHolder);
        return Integer.valueOf(keyHolder.getKeys().get("USER_ID").toString());
    }


    @Override
    public void insertAuthorities(final WinterUser user) {
        final String sql = "INSERT INTO AUTHORITY (USER_ID, AUTHORITY) VALUES (?, ?)";
        for (GrantedAuthority authority : user.getAuthorities()) {
            this.getJdbcTemplate().update(sql,
                    new Object[] { user.getUserId(), authority.getAuthority()});
        }
    }
    
    class WinterUserRowMapper implements RowMapper<WinterUser> {
        @Override
        public WinterUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            int userId = rs.getInt("USER_ID");
            String authoritiesSql = "SELECT AUTHORITY FROM AUTHORITY WHERE USER_ID=?";
            List<SimpleGrantedAuthority> authorities = JdbcUserDao.this.getJdbcTemplate().query(authoritiesSql,
                    new Object[] { userId }, new RowMapper<SimpleGrantedAuthority>() {
                                @Override
                                public SimpleGrantedAuthority mapRow(ResultSet resultSet, int arg1)
                                        throws SQLException {
                                    return new SimpleGrantedAuthority(resultSet.getString("AUTHORITY"));
                                }
                            });
            return new WinterUser(userId, 
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
