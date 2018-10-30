package com.meiye.service.user.impl;

import com.meiye.bo.role.AuthRolePermissionBo;
import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.user.UserBo;
import com.meiye.service.role.AuthUserService;
import com.meiye.service.store.StoreService;
import com.meiye.service.user.UserService;
import com.meiye.util.ObjectUtil;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jonyh on 08/08/18.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AuthUserService authUserService;

    @Autowired
    StoreService storeService;

    @Override
    public UserBo getUserByName(String userName,Long shopId) {
        AuthUserBo authUserBo=authUserService.getAuthUserBo(userName,shopId);
        if(authUserBo!=null) {
            UserBo userBo = new UserBo();
            userBo.setUsername(authUserBo.getAccount());
            userBo.setPassword(authUserBo.getPassword());
            userBo.setId(authUserBo.getId());
            userBo.setStoreBo(storeService.findStoreById(authUserBo.getShopIdenty()));
            List<SimpleGrantedAuthority> authorities=new ArrayList<SimpleGrantedAuthority>();
            if(authUserBo.getRoleBo()!=null&&authUserBo.getRoleBo().getAuthRolePermissions()!=null){
                authUserBo.getRoleBo().getAuthRolePermissions().forEach(authRolePermissionBo -> {
                    if(authRolePermissionBo.getAuthPermissionBo()!=null)
                        authorities.add(new SimpleGrantedAuthority(authRolePermissionBo.getAuthPermissionBo().getCode()));
                });
            }
            userBo.setAuthorities(authorities);
            return userBo;
        }else
            return null;
    }

    @Override
    public UserBo getUserById(Long userId, Long shopId,Long brandIdentity) {
        AuthUserBo authUserBo=authUserService.getOneByIdAndShopIdentity(userId,shopId,brandIdentity);
        if(authUserBo!=null) {
            UserBo userBo = new UserBo();
            userBo.setUsername(authUserBo.getAccount());
            userBo.setPassword(authUserBo.getPassword());
            userBo.setId(authUserBo.getId());
            userBo.setStoreBo(storeService.findStoreById(authUserBo.getShopIdenty()));
            List<SimpleGrantedAuthority> authorities=new ArrayList<SimpleGrantedAuthority>();
            if(authUserBo.getRoleBo()!=null&&authUserBo.getRoleBo().getAuthRolePermissions()!=null){
                authUserBo.getRoleBo().getAuthRolePermissions().forEach(authRolePermissionBo -> {
                    if(authRolePermissionBo.getAuthPermissionBo()!=null)
                        authorities.add(new SimpleGrantedAuthority(authRolePermissionBo.getAuthPermissionBo().getCode()));
                });
            }
            userBo.setAuthorities(authorities);
            return userBo;
        }else
            return null;
    }

}
