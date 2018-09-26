package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Data
public class YiPayCallBackRequestBo {
    private String code;
    private String trade_state;
    private String out_trade_no;
    private String trade_id;
    private String err_msg;
    private String pay_time;
    private String attach;
    private String pay_type;

    public boolean isSuccess(){
        return "0".equalsIgnoreCase(code);
    }

    public boolean isPaySuccess(){
        return  isSuccess()&&"1".equalsIgnoreCase(trade_state);
    }
}
