package com.meiye.service.user;

import com.meiye.bo.user.UserBo;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
public interface UserService {
    UserBo getUserByName(String userName, Long shopId);

    UserBo getUserById(Long userId, Long shopId);
}
