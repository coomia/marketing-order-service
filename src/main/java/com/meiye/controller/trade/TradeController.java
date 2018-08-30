package com.meiye.controller.trade;

import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.trade.TradeBo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/8/30 0030.
 */
@RestController
@RequestMapping(value = "/pos/api/trade",produces="application/json;charset=UTF-8")
public class TradeController {
    @PostMapping("/createOrder")
    public PosApiResult createOrder(@RequestBody TradeBo tradeBo){
        return PosApiResult.sucess(tradeBo);
    }
}
