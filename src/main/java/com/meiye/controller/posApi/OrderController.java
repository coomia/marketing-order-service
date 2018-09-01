package com.meiye.controller.posApi;

import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.posApi.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 22:34 2018/9/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/pos/api",produces="application/json;charset=UTF-8")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;

    @GetMapping("/testLog")
    public ResetApiResult testLog(){
        logger.info("order controller info ---");
        logger.error("order controller error ---");
        orderService.testLog();
        return ResetApiResult.sucess("");
    }



}
