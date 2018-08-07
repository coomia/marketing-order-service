package com.meiye.service.user.impl;

import com.meiye.bo.user.UserBo;
import com.meiye.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserBo getUserByName(String userName){
        UserBo userBo=new UserBo();
        userBo.setUsername("admin");
        userBo.setPassword("123456");
        List<SimpleGrantedAuthority> roleList=new ArrayList<SimpleGrantedAuthority>();
        roleList.add(new SimpleGrantedAuthority("manager"));
        roleList.add(new SimpleGrantedAuthority("caller"));
        userBo.setAuthorities(roleList);
        return userBo;
    }
}
