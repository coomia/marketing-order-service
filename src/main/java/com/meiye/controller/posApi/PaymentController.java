package com.meiye.controller.posApi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/9/22 0022.
 */
@RestController
@RequestMapping(value = "/pos/api/pay",produces="application/json;charset=UTF-8")
public class PaymentController {

    @PostMapping("/start")
    public void startPay(){

    }
}
