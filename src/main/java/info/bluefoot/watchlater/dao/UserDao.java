package info.bluefoot.watchlater.dao;

import info.bluefoot.watchlater.model.WatchLaterUser;

import java.util.List;

public interface UserDao {
    WatchLaterUser getUserByOpenId(String openid);

    int insertUser(WatchLaterUser user);

    void insertAuthorities(WatchLaterUser user);

    List<WatchLaterUser> getAll();
}
