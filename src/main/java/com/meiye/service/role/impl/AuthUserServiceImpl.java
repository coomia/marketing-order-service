package com.meiye.service.role.impl;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.model.role.AuthUser;
import com.meiye.repository.role.AuthUserRepository;
import com.meiye.service.role.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 13:12 2018/8/18
 * @Modified By:
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Override
    public void AddAuthUser(AuthUserBo authUserBo) {
        AuthUser authUser = authUserBo.copyTo(AuthUser.class);
        authUserRepository.save(authUser);
    }

    @Override
    public AuthUserBo findLatestAuthUser() {
        AuthUser authUser = authUserRepository.findFirst1OrderByServerCreateTimeDesc();
        return authUser.copyTo(AuthUserBo.class);
    }
}
