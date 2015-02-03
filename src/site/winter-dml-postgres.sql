-- Sequence: "PLAYLIST_PLAYLIST_ID_seq"

-- DROP SEQUENCE "PLAYLIST_PLAYLIST_ID_seq";

CREATE SEQUENCE "PLAYLIST_PLAYLIST_ID_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE "PLAYLIST_PLAYLIST_ID_seq"
  OWNER TO postgres;


-- Sequence: "USER_USER_ID_seq"

-- DROP SEQUENCE "USER_USER_ID_seq";

CREATE SEQUENCE "USER_USER_ID_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE "USER_USER_ID_seq"
  OWNER TO winter;

-- Sequence: "VIDEO_VIDEO_ID_seq"

-- DROP SEQUENCE "VIDEO_VIDEO_ID_seq";

CREATE SEQUENCE "VIDEO_VIDEO_ID_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE "VIDEO_VIDEO_ID_seq"
  OWNER TO postgres;


  -- Table: "user"

  -- DROP TABLE users;

CREATE TABLE users
(
  user_id integer NOT NULL DEFAULT nextval('"USER_USER_ID_seq"'::regclass),
  username character varying NOT NULL,
  password character varying,
  full_name character varying NOT NULL,
  email character varying NOT NULL,
  openid character varying,
  is_openid_user boolean NOT NULL,
  CONSTRAINT "PK_USERID" PRIMARY KEY (user_id),
  CONSTRAINT "UNIQ_EMAIL" UNIQUE (email),
  CONSTRAINT "UNIQ_USERNAME" UNIQUE (username),
  CONSTRAINT "USER_OPENID_key" UNIQUE (openid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO winter;


-- Table: authority

-- DROP TABLE authority;

CREATE TABLE authority
(
  user_id integer NOT NULL,
  authority character varying NOT NULL,
  CONSTRAINT "AUTHORITY_pkey" PRIMARY KEY (user_id, authority),
  CONSTRAINT authority_user_id_fkey FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE authority
  OWNER TO postgres;


-- Table: video

-- DROP TABLE video;

CREATE TABLE video
(
  video_id integer NOT NULL DEFAULT nextval('"VIDEO_VIDEO_ID_seq"'::regclass),
  current_position integer,
  url character varying NOT NULL,
  sort_position integer NOT NULL,
  playlist_id integer NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE video
  OWNER TO postgres;


-- Table: playlist

-- DROP TABLE playlist;

CREATE TABLE playlist
(
  playlist_id integer NOT NULL DEFAULT nextval('"PLAYLIST_PLAYLIST_ID_seq"'::regclass),
  name character varying NOT NULL,
  image character varying NOT NULL,
  last_reprod_video_id integer,
  user_id integer NOT NULL,
  CONSTRAINT "PLAYLIST_pkey" PRIMARY KEY (playlist_id),
  CONSTRAINT "PLAYLIST_last_reprod_video_id_fkey" FOREIGN KEY (last_reprod_video_id)
      REFERENCES video (video_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE playlist
  OWNER TO postgres;

-- adding playlist_id to video

ALTER TABLE video
  ADD COLUMN playlist_id integer NOT NULL;
  
  ALTER TABLE video
    ADD CONSTRAINT video_playlist_id_fkey FOREIGN KEY (playlist_id)
        REFERENCES playlist (playlist_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;
