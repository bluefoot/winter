package info.bluefoot.watchlater.dao.jdbc;

import info.bluefoot.watchlater.dao.PlaylistDao;
import info.bluefoot.watchlater.model.Playlist;
import info.bluefoot.watchlater.model.Video;

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
    public List<Playlist> getPlaylistsByUser(final String username) {
        String sql = "SELECT P.PLAYLIST_ID, P.NAME, P.IMAGE, P.LAST_REPROD_VIDEO_ID FROM PLAYLIST P INNER JOIN USERS U ON (U.USER_ID=P.USER_ID) WHERE U.USERNAME=? ORDER BY P.NAME";
        return this.getJdbcTemplate().query(sql, new Object[] {username}, new RowMapper<Playlist>() {
            @Override
            public Playlist mapRow(ResultSet resultSet, int arg1)
                    throws SQLException {
                Playlist pl = new Playlist();
                pl.setPlaylistId(resultSet.getInt("playlist_id"));
                pl.setName(resultSet.getString("name"));
                pl.setImage(resultSet.getString("image"));
                pl.setUser(username);
                if(resultSet.getInt("LAST_REPROD_VIDEO_ID")!=0) {
                    pl.setLastPlayed(new Video(resultSet.getInt("LAST_REPROD_VIDEO_ID")));
                }
                return pl;
            }
        });
    }

    @Override
    public List<Video> getVideosFromPlaylist(Integer id) {
        String sql = "SELECT V.VIDEO_ID, V.URL, V.LAST_PLAYED_TIME FROM VIDEO V INNER JOIN VIDEO_PLAYLIST VP ON (VP.VIDEO_ID=V.VIDEO_ID) WHERE VP.PLAYLIST_ID=?";
        return this.getJdbcTemplate().query(sql, new Object[] {id}, new RowMapper<Video>() {
            @Override
            public Video mapRow(ResultSet resultSet, int arg1)
                    throws SQLException {
                Video v = new Video();
                v.setVideoId(resultSet.getInt("video_id"));
                v.setUrl(resultSet.getString("url"));
                v.setLastPlayedTime(resultSet.getInt("LAST_PLAYED_TIME"));
                return v;
            }
        });
    }

    @Override
    public void updateLastPlayedVideo(Integer playlistId, Integer videoId) {
        String sql = "UPDATE PLAYLIST SET last_reprod_video_id = ? WHERE PLAYLIST_ID = ?";
        this.getJdbcTemplate().update(sql, new Object[] {videoId, playlistId});
    }

    @Override
    public void addPlaylistAndVideoAssociation(final Playlist playlist) {
        final String sqlInsertPlaylist = "INSERT INTO PLAYLIST (IMAGE, NAME, USERNAME) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlInsertPlaylist, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, playlist.getImage().trim());
                ps.setString(2, playlist.getName().trim());
                ps.setString(3, playlist.getUser().trim());
                return ps;
            }
        }, keyHolder);
        playlist.setPlaylistId(keyHolder.getKey().intValue());

        String sqlInsertPlaylistVideo = "INSERT INTO video_playlist (VIDEO_ID, PLAYLIST_ID) VALUES(?, ?)";
        for (Video video : playlist.getVideos()) {
            this.getJdbcTemplate().update(sqlInsertPlaylistVideo,
                                    new Object[] { video.getVideoId(),
                                    playlist.getPlaylistId() });
        }
    }
}
