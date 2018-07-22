package com.igoso.me.gallery.dao;

import com.igoso.me.gallery.entity.AuthUser;
import org.springframework.stereotype.Repository;

/**
 * created by igoso at 2018/7/21
 **/

@Repository
public interface AuthUserDao extends BaseDao<AuthUser> {

    Long doLogin(AuthUser user);
}
