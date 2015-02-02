package info.bluefoot.watchlater.dao.jdbc;

import info.bluefoot.watchlater.dao.VideoDao;
import info.bluefoot.watchlater.model.Video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcVideoDao extends JdbcDaoSupport  implements VideoDao {

    @Override
    public void updateNotNullValues(Video video) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE VIDEO SET");
        List<Object> parameters = new ArrayList<Object>();
        if(video.getUrl()!=null) {
            sql.append(" URL = ? ");
            parameters.add(video.getUrl());
        }
        if(video.getLastPlayedTime()!=null) {
            sql.append(" LAST_PLAYED_TIME = ? ");
            parameters.add(video.getLastPlayedTime());
        }
        sql.append(" WHERE VIDEO_ID = ?");
        parameters.add(video.getVideoId());
        this.getJdbcTemplate().update(sql.toString(), parameters.toArray(new Object[parameters.size()]));
    }

    @Override
    public int insert(final Video video) {
        final String sql = "INSERT INTO VIDEO (URL) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
//        this.getJdbcTemplate().update(sql, new Object[] {video.getUrl()}, keyHolder);
        //this.getJdbcTemplate().update(psc, generatedKeyHolder)
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        //PreparedStatement ps = connection.prepareStatement(sql, new String[] {"video_id"}, Statement.RETURN_GENERATED_KEYS);
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, video.getUrl());
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public List<Video> getAll() {
        String sql = "SELECT VIDEO_ID, URL, last_played_time FROM VIDEO";
        return this.getJdbcTemplate().query(sql, new RowMapper<Video>() {
            @Override
            public Video mapRow(ResultSet resultSet, int arg1)
                    throws SQLException {
                Video v = new Video();
                v.setVideoId(resultSet.getInt("VIDEO_ID"));
                v.setUrl(resultSet.getString("URL"));
                v.setLastPlayedTime(resultSet.getInt("last_played_time"));
                return v;
            }
        });
    }

}
