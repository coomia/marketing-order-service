package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@Data
public class PrePayReturnBo {
    private String payRequestType;//MicroPay --- 小程序支付; ScanPay--商户扫用户二维码支付; ScanQrPay -- 用户扫二维码支付
    private String outTradeNo;//
    private String authCode;//ScanPay专用
    private Integer tradeAmout;
    private String wechatAppid;
    private String wechatOpenId;
    private String payType;//调用翼支付时的支付方式，0--微信，1--支付宝；3--翼支付
    private boolean needYiPay=false;
}
