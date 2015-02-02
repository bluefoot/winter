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
    
create table users(
    user_id integer IDENTITY not null primary key,
    username varchar(1000) not null,
    password varchar(50),
    full_name varchar(50) not null,
    email varchar(1000) not null,
    openid varchar(1000),
    IS_OPENID_USER boolean not null,
    );
    create unique index ix_users_username on users (username);
    create unique index ix_users_openid on users (openid);
    create unique index ix_users_email on users (email);
    
create table authorities (
    user_id integer not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key(user_id) references users(user_id));
    create unique index ix_auth_username on authorities (user_id,authority);
      
create table video(
    video_id integer IDENTITY,
    last_played_time integer,
    url varchar(200) not null);
    --int NOT NULL primary key IDENTITY
--ALTER TABLE video ALTER COLUMN video_id 
--SET GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1);

create table playlist(
    playlist_id integer IDENTITY,
    name varchar(200) not null,
    image varchar(200),
    last_reprod_video_id integer,
    last_reprod_time integer,
    user_id integer not null,
    constraint fk_playlist_last_reprod_video foreign key(last_reprod_video_id) references video(video_id),
    constraint fk_playlist_username foreign key(user_id) references users(user_id));
--ALTER TABLE playlist ALTER COLUMN playlist_id 
--SET GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1);

create table video_playlist(
    video_id integer not null,
    playlist_id integer not null,
    constraint fk_video_playlist_video foreign key(video_id) references video(video_id),
    constraint fk_video_playlist_playlist foreign key(playlist_id) references playlist(playlist_id));
    create unique index ix_video_playlist on video_playlist (video_id,playlist_id);

create table watched_video(
    video_id integer not null,
    user_id integer not null,
    constraint fk_watched_video_video foreign key(video_id) references video(video_id),
    constraint fk_watched_video_users foreign key(user_id) references users(user_id));
    create unique index ix_watched_video on watched_video (video_id,user_id);