package info.bluefoot.winter.dao.jdbc;

import info.bluefoot.winter.dao.PlaylistDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.Video;
import info.bluefoot.winter.model.WinterUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPlaylistDao extends JdbcDaoSupport implements PlaylistDao  {

    @Override
    public List<Playlist> getPlaylistsByUser(final WinterUser user) {
        String sql = "SELECT P.PLAYLIST_ID, P.NAME, P.IMAGE, P.LAST_REPROD_VIDEO_ID FROM PLAYLIST P INNER JOIN USERS U ON (U.USER_ID=P.USER_ID) WHERE U.USER_ID=? ORDER BY P.NAME";
        return this.getJdbcTemplate().query(sql, new Object[] {user.getUserId()}, new RowMapper<Playlist>() {
            @Override
            public Playlist mapRow(ResultSet resultSet, int arg1)
                    throws SQLException {
                Playlist playlist = new Playlist();
                playlist.setPlaylistId(resultSet.getInt("PLAYLIST_ID"));
                playlist.setName(resultSet.getString("NAME"));
                playlist.setImage(resultSet.getString("IMAGE"));
                if(resultSet.getInt("LAST_REPROD_VIDEO_ID")!=0) {
                    playlist.setLastReproduced(new Video(resultSet.getInt("LAST_REPROD_VIDEO_ID")));
                }
                return playlist;
            }
        });
    }

    @Override
    public void updateLastPlayedVideo(Integer playlistId, Integer videoId) {
        String sql = "UPDATE PLAYLIST SET LAST_REPROD_VIDEO_ID = ? WHERE PLAYLIST_ID = ?";
        this.getJdbcTemplate().update(sql, new Object[] {videoId, playlistId});
    }

    @Override
    public Playlist addPlaylist(final Playlist playlist) {
        final String sqlInsertPlaylist = "INSERT INTO PLAYLIST (NAME, IMAGE, USER_ID) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlInsertPlaylist, 
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, playlist.getName().trim());
                ps.setString(2, playlist.getImage().trim());
                ps.setInt(3, playlist.getUser().getUserId());
                return ps;
            }
        }, keyHolder);
        playlist.setPlaylistId(Integer.valueOf(keyHolder.getKeys().get("PLAYLIST_ID").toString()));
        return playlist;
    }
}
