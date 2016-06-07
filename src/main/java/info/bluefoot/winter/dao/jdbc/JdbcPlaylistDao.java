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

import info.bluefoot.winter.dao.PlaylistDao;
import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.User;
import info.bluefoot.winter.model.Video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPlaylistDao extends JdbcDaoSupport implements PlaylistDao  {

    @Inject 
    public JdbcPlaylistDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }
    
    @Override
    public List<Playlist> getPlaylistsByUser(final User user) {
        String sql = "SELECT P.PLAYLIST_ID, P.NAME, P.IMAGE, P.LAST_REPROD_VIDEO_ID FROM PLAYLIST P WHERE P.USERNAME=? ORDER BY P.NAME";
        return this.getJdbcTemplate().query(sql, new Object[] {user.getUsername()}, new RowMapper<Playlist>() {
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
        final String sqlInsertPlaylist = "INSERT INTO PLAYLIST (NAME, IMAGE, USERNAME) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlInsertPlaylist, 
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, playlist.getName().trim());
                ps.setString(2, playlist.getImage().trim());
                ps.setString(3, playlist.getUser().getUsername());
                return ps;
            }
        }, keyHolder);
        playlist.setPlaylistId(Integer.valueOf(keyHolder.getKeys().get("PLAYLIST_ID").toString()));
        return playlist;
    }

    @Override
    public void removePlaylist(Integer playlistId) {
        String sql = "DELETE FROM PLAYLIST WHERE PLAYLIST_ID = ?";
        this.getJdbcTemplate().update(sql, new Object[] {playlistId});
    }

    @Override
    public Playlist getPlaylistsByIdAndUser(Integer playlistId, String username) {
        String sql = "SELECT PLAYLIST_ID, NAME, IMAGE, LAST_REPROD_VIDEO_ID FROM PLAYLIST WHERE PLAYLIST_ID = ? AND USERNAME = ?";
        return this.getJdbcTemplate().queryForObject(sql,
                new Object[] { playlistId, username }, new RowMapper<Playlist>() {
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
    public Playlist getPlaylistsById(Integer playlistId) {
        String sql = "SELECT PLAYLIST_ID, NAME, IMAGE, LAST_REPROD_VIDEO_ID FROM PLAYLIST WHERE PLAYLIST_ID = ?";
        return this.getJdbcTemplate().queryForObject(sql,
                new Object[] { playlistId }, new RowMapper<Playlist>() {
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
}
