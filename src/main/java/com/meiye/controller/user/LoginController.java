package com.meiye.controller.user;

import com.meiye.bo.system.ResetApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/8/6 0006.
 */

@RestController
public class LoginController {

    @PostMapping(value="/login",produces="application/json;charset=UTF-8")
    public ResetApiResult login(){
        return ResetApiResult.sucess("");
    }

}
