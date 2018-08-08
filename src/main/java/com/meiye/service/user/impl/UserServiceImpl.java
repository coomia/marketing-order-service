package com.meiye.service.user.impl;

import com.meiye.bo.user.UserBo;
import com.meiye.service.user.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonyh on 08/08/18.
 */
@Component
@Service
public class UserServiceImpl implements UserDetailsService,UserService {
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserBo userBo=new UserBo();
        userBo.setUsername(userName);
        userBo.setPassword("123456");
        List<SimpleGrantedAuthority> roles=new ArrayList<SimpleGrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("Manager"));
        roles.add(new SimpleGrantedAuthority("Skiller"));
        return userBo;
    }

    @Override
    public UserBo getUserByName(String userName) {
        return null;
    }
}
