package info.bluefoot.winter.dao.jdbc;

import info.bluefoot.winter.dao.VideoDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;

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
        if(video.getCurrentPosition()!=null) {
            sql.append(" CURRENT_POSITION = ? ");
            parameters.add(video.getCurrentPosition());
        }
        sql.append(" WHERE VIDEO_ID = ?");
        parameters.add(video.getVideoId());
        this.getJdbcTemplate().update(sql.toString(), parameters.toArray(new Object[parameters.size()]));
    }

    @Override
    public int insert(final Video video) {
        final String sql = "INSERT INTO VIDEO (URL, PLAYLIST_ID, SORT_POSITION) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, video.getUrl());
                        ps.setInt(2, video.getPlaylist().getPlaylistId());
                        ps.setInt(3, video.getSortPosition());
                        return ps;
                    }
                },
                keyHolder);
        return Integer.valueOf(keyHolder.getKeys().get("VIDEO_ID").toString());
    }
    

    @Override
    public List<Video> getVideosFromPlaylist(Playlist playlist) {
        String sql = "SELECT V.VIDEO_ID, V.URL, V.CURRENT_POSITION, V.SORT_POSITION FROM VIDEO V WHERE V.PLAYLIST_ID=? ORDER BY V.SORT_POSITION";
        return this.getJdbcTemplate().query(sql, new Object[] {playlist.getPlaylistId()}, new RowMapper<Video>() {
            @Override
            public Video mapRow(ResultSet resultSet, int arg1) throws SQLException {
                Video v = new Video();
                v.setVideoId(resultSet.getInt("VIDEO_ID"));
                v.setUrl(resultSet.getString("URL"));
                v.setCurrentPosition(resultSet.getInt("CURRENT_POSITION"));
                v.setSortPosition(resultSet.getInt("SORT_POSITION"));
                return v;
            }
        });
    }

}
