package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/16 0016.
 */
@Data
public class MicroAppPayRequestBo {
    private String appid;//每个商户自己的 appid
    private String sign;//
    private Integer total_amount;//支付金额
    private String out_trade_no;//商户订单号
    private String return_url;//回调地址
    private String sub_appid;//微信小程序 ID
    private String sub_openid;//用户标识
    private String spbill_create_ip;//终端 IP
    private String nonce_str;//随机数
    private String version;//版本号
}
