package info.bluefoot.winter.dao;

import info.bluefoot.winter.model.WinterUser;

public interface UserDao {
    WinterUser getUserByOpenId(String openid);

    int insertUser(WinterUser user);

    void insertAuthorities(WinterUser user);
}
