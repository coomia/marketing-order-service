package com.meiye.service.user.impl;

import com.meiye.bo.user.UserBo;
import com.meiye.service.user.UserService;
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

    @Override
    public UserBo getUserByName(String userName) {
        UserBo userBo=new UserBo();
        userBo.setUsername(userName);
        userBo.setPassword("123456");
        userBo.setServerCreateTime(new Timestamp(System.currentTimeMillis()));
        List<SimpleGrantedAuthority> roles=new ArrayList<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_Manager"));
        roles.add(new SimpleGrantedAuthority("ROLE_Skiller"));

        userBo.setAuthorities(roles);
        return userBo;
    }
}
