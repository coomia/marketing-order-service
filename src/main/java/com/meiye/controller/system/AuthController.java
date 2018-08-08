package com.meiye.controller.system;

import com.meiye.annotation.CurrentUser;
import com.meiye.bo.user.UserBo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2018/8/7 0007.
 */
@Controller
@RequestMapping
public class AuthController {
    @RequestMapping("/auth/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error){
        if(error!=null)
            model.addAttribute("error","用户名或密码错误");
        return "/auth/login";
    }

    @RequestMapping("/meiyeSys")
    public String index(Model model,@CurrentUser UserBo userBo){
        model.addAttribute("user",userBo);
        return "main";
    }
}
