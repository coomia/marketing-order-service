package com.meiye.bo.pay;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@Data
public class PrePayBo {
    private String payRequestType;//MicroPay --- 小程序支付; ScanPay--商户扫用户二维码支付; ScanQrPay -- 用户扫二维码支付
    private String outTradeNo;//商家支付单号,payment_item.uuid
    private String authCode;//ScanPay专用
    private Double tradeAmount; //支付金额
    private Integer tradeAmountInCent;//支付金额，按分算
    private String wechatAppid; // MicroPay ---- 小程序支付专用
    private String wechatOpenId;// MicroPay ---- 小程序支付专用
    private Integer payType=10;//调用翼支付时的支付方式，0--微信，1--支付宝；3--翼支付, 4 -- 余额, 5 -- 现金, 10 --- 其他
    private boolean needYiPay=false;
    private Long paymentItemId;

    public boolean isWechatPay(){
        return payType==null?false:payType.equals(0);
    }
    public void setWechatPay(){
        this.payType=0;
    }

    public boolean isAliPay(){
        return payType==null?false:payType.equals(1);
    }
    public void setAliPay(){
        this.payType=1;
    }

    public boolean isYiPay(){
        return payType==null?false:payType.equals(3);
    }
    public void setYiPay(){
        this.payType=3;
    }

    public boolean isBanlancePay(){
        return payType==null?false:payType.equals(4);
    }
    public void setBanlancePay(){
        this.payType=4;
    }

    public boolean isCashPay(){
        return payType==null?false:payType.equals(5);
    }
    public void setCashPay(){
        this.payType=5;
    }

    public void setTradeAmount(Double tradeAmount){
        this.tradeAmount=tradeAmount;
        if(this.tradeAmount!=null){
            Double tradeAmt = this.tradeAmount * 100d;
            this.setTradeAmountInCent(tradeAmt.intValue());
        }
    }

    public void setTradeAmountInCent(Integer tradeAmountInCent){
        this.tradeAmountInCent=tradeAmountInCent;
        this.setTradeAmount(tradeAmountInCent==null?null:tradeAmountInCent/100d);
    }

}
