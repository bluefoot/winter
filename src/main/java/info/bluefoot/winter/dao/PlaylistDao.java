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
package info.bluefoot.winter.dao;

import info.bluefoot.winter.model.Playlist;
import info.bluefoot.winter.model.User;

import java.util.List;

public interface PlaylistDao {
    public List<Playlist> getPlaylistsByUser(User user);

    public void updateLastPlayedVideo(Integer playlistId, Integer videoId);

    public Playlist addPlaylist(Playlist playlist);

    public void removePlaylist(Integer playlistId);

    public Playlist getPlaylistsByIdAndUser(Integer playlistId, String username);

    public Playlist getPlaylistsById(Integer playlistId);
}
