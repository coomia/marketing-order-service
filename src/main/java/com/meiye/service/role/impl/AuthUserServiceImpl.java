package com.meiye.service.role.impl;

import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.user.UserBo;
import com.meiye.model.role.AuthUser;
import com.meiye.repository.role.AuthUserRepository;
import com.meiye.service.role.AuthRoleService;
import com.meiye.service.role.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    AuthRoleService authRoleService;

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void addAuthUser(AuthUserBo authUserBo) {
        AuthUser authUser = authUserBo.copyTo(AuthUser.class);
        authUserRepository.save(authUser);
    }

    @Override
    public AuthUserBo findLatestAuthUser() {
        AuthUser authUser = authUserRepository.findFirstByOrderByServerCreateTimeDesc();
        return authUser.copyTo(AuthUserBo.class);
    }

    @Override
    public AuthUserBo getAuthUserBo(String userName){
        if(userName!=null){
            AuthUser authUser=authUserRepository.findFirstByAccount(userName);
            AuthUserBo authUserBo=authUser==null?null:authUser.copyTo(AuthUserBo.class);
            if(authUserBo!=null){
                authUserBo.setRoleBo(authRoleService.findOneById(authUserBo.getRoleId()));
            }
            return authUserBo;
        }
        return null;
    }
}
