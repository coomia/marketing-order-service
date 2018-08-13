package com.meiye.controller.user;

import com.meiye.bo.system.ResetApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/8/6 0006.
 */

@RestController
public class LoginController {

    @RequestMapping("/login")
    public ResetApiResult login(){
        return ResetApiResult.sucess("");
    }

}
