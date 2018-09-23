package com.meiye.service.pay.impl;

import com.meiye.service.pay.PayService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Service
public class PayServiceImpl implements PayService {
    @Override
    public String getAppSercet(Integer storeId){
        return "test";
    }
}
