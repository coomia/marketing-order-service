package com.meiye.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/8/7 0007.
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @RequestMapping("/login")
    public String login(){
        return "login";
    }


}
