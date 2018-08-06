package com.meiye.controller.user;

import com.meiye.bo.user.UserBo;
import com.meiye.service.user.UserService;
import com.meiye.system.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/8/6 0006.
 */

@RestController
@RequestMapping(path = "/user")
public class LoginController {

    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthUtils authUtils;

    @RequestMapping(value = "/login")
    public String login(@RequestParam(value  = "userName") String userName,@RequestParam(value  = "password") String password){
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
//        Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserBo userBo = userService.getUserByName("");
        return authUtils.generateToken(userBo);
    }
}
