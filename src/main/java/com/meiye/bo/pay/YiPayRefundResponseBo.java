package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Data
public class YiPayRefundResponseBo {
    private String code;
    private String haipay_refund_id;//退款单号
    private String err_msg;
    private String refund_time;
    private String pay_type;

    public boolean isSuccess(){
        return "0".equalsIgnoreCase(code);
    }
}
