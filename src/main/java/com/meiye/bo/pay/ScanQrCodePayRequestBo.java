package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Data
public class ScanQrCodePayRequestBo {
    private String appid;
    private String sign;
    private Integer total_amount;
    private Integer discountable_amount;
    private String discount_coupon;
    private String body;
    private String detail;
    private String attach;
    private String nonce_str;
    private String out_trade_no;
    private String version;
    private String return_url;
    private String openid;
    private String note;


}
