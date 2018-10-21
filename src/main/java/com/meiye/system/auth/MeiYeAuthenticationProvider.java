package com.meiye.system.auth;

import com.meiye.bo.system.LoginBo;
import com.meiye.bo.user.UserBo;
import com.meiye.service.user.UserService;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public class MeiYeAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    Logger logger= LoggerFactory.getLogger(MeiYeAuthenticationProvider.class);


    public MeiYeAuthenticationProvider(UserService userService){
        this.userService=userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        LoginBo loginBo=null;
        Long storeId=null;
        UserBo userBo=null;
        try {
            if (authentication.getDetails() != null) {
                loginBo = (LoginBo) authentication.getDetails();
                if (loginBo != null)
                    storeId = Long.parseLong(loginBo.getStoreId());
            }
            password = new Sha1Hash(password, name, 100).toHex();

            userBo = userService.getUserByName(name, storeId);
        } catch (Exception exp) {
            logger.error(exp.getMessage(),exp);
            throw new AuthenticationServiceException("未知错误");
        }
        // 认证逻辑
        if (userBo == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(userBo.getUsername())) {
            throw new UsernameNotFoundException("用户不存在");
        } else if (userBo != null && name.equals(userBo.getUsername()) && password.equals(userBo.getPassword())) {
            // 生成令牌
            userBo.setPassword(null);
            Authentication auth = new UsernamePasswordAuthenticationToken(userBo, null, userBo.getAuthorities());
            return auth;
        } else if (userBo != null && name.equals(userBo.getUsername()) && !password.equals(userBo.getPassword())) {
            throw new BadCredentialsException("密码错误");
        } else {
            throw new AuthenticationServiceException("未知错误");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
