package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Data
public class SyncPayStatusResponseBo {
    private String code;
    private String err_msg;
    private String trade_state;//0：未支付;1：支付成功;2：支付失败;3：已退款;4：已撤销
    private Integer total_amount;//支付金额，单位：分，该支付金额是实际金额。
    private Integer refund_amount;//退款金额，单位：分，实际退款金额
    private String out_trade_no;//商户订单号
    private String trade_id;
    private String pay_time;
    private String discountable_amount;
    private String discount_coupon;
    private String body;
    private String detail;
    private String pay_type;
    private String attach;
    private String note;

    public boolean isSuccess(){
        return "0".equalsIgnoreCase(code);
    }

    public boolean isPaySuccess(){
        return isSuccess()&&"1".equalsIgnoreCase(trade_state);
    }

    public boolean isPayFailed(){
        return isSuccess()&&("2".equalsIgnoreCase(trade_state)||"4".equalsIgnoreCase(trade_state));
    }

    public boolean isRefundSuccess(){
        return isSuccess()&&"3".equalsIgnoreCase(trade_state);
    }
}
