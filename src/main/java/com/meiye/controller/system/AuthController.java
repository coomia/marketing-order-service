package com.meiye.controller.system;

import com.meiye.annotation.CurrentUser;
import com.meiye.bo.user.UserBo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2018/8/7 0007.
 */
@Controller
@RequestMapping(produces="application/json;charset=UTF-8")
public class AuthController {
    @PostMapping("/auth/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error){
        if(error!=null)
            model.addAttribute("error","用户名或密码错误");
        return "/auth/login";
    }

    @RequestMapping("/index")
    public String index(Model model,@CurrentUser UserBo userBo){
        model.addAttribute("user",userBo);
        return "index";
    }
}
