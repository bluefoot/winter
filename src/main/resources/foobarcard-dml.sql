-- Copyright 2011 Gewton Jhames <gewtonj@gmail.com>
-- 
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--     http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

insert into users(user_id, username, password, full_name, email, openid, isopeniduser) values ('2', 'gewton', 'gewton', 'Gewton Normal User', 'gewtonj@gmail.com', null, false);
insert into users(user_id, username, password, full_name, email, openid, isopeniduser) values ('1', 'https://www.google.com/accounts/o8/id?id=AItOawkPCAXt67l_GFkwfsjGulbz8WCgH7OOdiU', null, 'Gewton Teixeira', 'gjhames@gmail.com', 'https://www.google.com/accounts/o8/id?id=AItOawkPCAXt67l_GFkwfsjGulbz8WCgH7OOdiU', true);
insert into users(user_id, username, password, full_name, email, openid, isopeniduser) values ('3', 'adama', 'adama', 'Adama normal user', 'adamaasdfasdf@gmail.com', null, false);

insert into authorities(user_id, authority) values ('1', 'ROLE_USER');
insert into authorities(user_id, authority) values ('2', 'ROLE_USER');
insert into authorities(user_id, authority) values ('3', 'ROLE_USER');
insert into authorities(user_id, authority) values ('3', 'ROLE_ADMIN');

insert into video(video_id, url, last_played_time) values ('1', 'https://www.youtube.com/watch?v=I8pY0Z7mq8I', '40');
insert into video(video_id, url, last_played_time) values ('2', 'https://www.youtube.com/watch?v=k3LBfITvkBk', '60');
insert into video(video_id, url) values ('3', 'https://www.youtube.com/watch?v=azu48kGR_Ro');
insert into video(video_id, url, last_played_time) values ('4', 'http://www.twitch.tv/dragon/v/3633912', '67544');
insert into video(video_id, url) values ('5', 'http://www.twitch.tv/dragon/v/3720000');
insert into video(video_id, url) values ('6', 'http://www.twitch.tv/sc2_starbow/v/3727852');
insert into video(video_id, url) values ('7', 'http://www.twitch.tv/day9tv/c/5974588');

insert into playlist(playlist_id, image, name, user_id) values ('1', '//i.ytimg.com/vi/OSRYcUBtlU0/mqdefault.jpg', 'single playlist test', '1');
insert into playlist(playlist_id, image, name, user_id) values ('2', 'https://i.ytimg.com/vi/2vw_-uylxNE/mqdefault.jpg', 'openid playlist', '2');
insert into playlist(playlist_id, image, name, last_reprod_video_id, user_id) values ('3', 'https://i.ytimg.com/vi/zf-J0RRvLO8/mqdefault.jpg', 'three video playlist', '4', '1');
insert into playlist(playlist_id, image, name, user_id) values ('4', 'https://i.ytimg.com/vi/LMLwslRNThM/mqdefault.jpg', 'anoter pl', '1');

insert into video_playlist(video_id, playlist_id) values ('1', '1'); --playlist 1
insert into video_playlist(video_id, playlist_id) values ('7', '1'); --playlist 1
insert into video_playlist(video_id, playlist_id) values ('2', '2'); --playlist 2
insert into video_playlist(video_id, playlist_id) values ('3', '2'); --playlist 2
insert into video_playlist(video_id, playlist_id) values ('1', '3'); --playlist 3
insert into video_playlist(video_id, playlist_id) values ('2', '3'); --playlist 3
insert into video_playlist(video_id, playlist_id) values ('4', '3'); --playlist 3
insert into video_playlist(video_id, playlist_id) values ('3', '3'); --playlist 3
insert into video_playlist(video_id, playlist_id) values ('5', '3'); --playlist 3
insert into video_playlist(video_id, playlist_id) values ('6', '3'); --playlist 3