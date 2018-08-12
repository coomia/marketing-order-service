package com.meiye.system.auth;

import com.meiye.bo.user.UserBo;
import com.meiye.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public class MeiYeAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;


    public MeiYeAuthenticationProvider(UserService userService){
        this.userService=userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserBo userBo=userService.getUserByName(name);

        // 认证逻辑
        if(userBo==null || StringUtils.isEmpty(name) ||StringUtils.isEmpty(password)||StringUtils.isEmpty(userBo.getUsername())){
            throw new UsernameNotFoundException("用户名不存在");
        } else if (userBo!=null&&name.equals(userBo.getUsername())&&password.equals(userBo.getPassword())) {
            // 生成令牌
            userBo.setPassword(null);
            Authentication auth = new UsernamePasswordAuthenticationToken(userBo,null, userBo.getAuthorities());
            return auth;
        }else if (userBo!=null&&name.equals(userBo.getUsername())&&!password.equals(userBo.getPassword()))  {
            throw new BadCredentialsException("密码错误");
        }else {
            throw new UnknownError("未知错误");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
