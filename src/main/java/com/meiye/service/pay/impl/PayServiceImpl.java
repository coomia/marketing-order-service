package com.meiye.service.pay.impl;

import com.meiye.exception.BusinessException;
import com.meiye.meiyeenum.PayType;
import com.meiye.model.trade.Trade;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Service
public class PayServiceImpl implements PayService {
    @Autowired
    OrderService orderService;

    @Override
    public String getAppSercet(Integer storeId){
        return "test";
    }


    //TODO 验证支付
    public void prePay(){

    }

    public void afterYiPaySuccess(String tradeNo, String yiPayTradeNo, PayType payType){
        Trade trade=orderService.getTradeByTradeNo(tradeNo);
        if(trade==null)
            throw new BusinessException("交易记录不存在.");

    }

}
