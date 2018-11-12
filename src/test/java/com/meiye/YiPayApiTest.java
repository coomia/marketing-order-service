package com.meiye;

import com.meiye.bo.pay.*;
import com.meiye.util.StringUtil;
import com.meiye.util.UUIDUtil;
import com.meiye.util.YiPayApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class YiPayApiTest {
//    private final String appid="hf19102035480OVA";
//    private final String appsecret="nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA";
    private final String callbackContextPath="http://182.139.217.242:7090/MeiYe";


    private final String appid="hf19102035480OVA";
    private final String appsecret="nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA";
    @Test
    public void testScanPay(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        PrePayBo prePayBo=new PrePayBo();
        prePayBo.setOutTradeNo( "101181017192442218000000");
        prePayBo.setTradeAmountInCent(1);
        prePayBo.setNeedYiPay(true);
        prePayBo.setAuthCode("asssss");
        prePayBo.setAliPay();
//        prePayBo.setWechatPay();
//        prePayBo.setWechatAppid("");
//        prePayBo.setWechatOpenId("");

        ScanPayResponseBo scanPayResponseBo=YiPayApi.scanPay(paymentParamBo,prePayBo);
        System.out.println(scanPayResponseBo.toString());
    }


    @Test
    public void testScanQrCodePay(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        PrePayBo prePayBo=new PrePayBo();
        prePayBo.setOutTradeNo( "101181017192442218000000");
        prePayBo.setTradeAmountInCent(1);
        prePayBo.setNeedYiPay(true);
        prePayBo.setAuthCode("asssss");
        prePayBo.setAliPay();
//        prePayBo.setWechatPay();
//        prePayBo.setWechatAppid("");
//        prePayBo.setWechatOpenId("");
        ScanQrCodePayResponseBo scanQrCodePayResponseBo=YiPayApi.getQrCodeForPay(paymentParamBo,prePayBo);
        System.out.println("Out trade no is:"+prePayBo.getOutTradeNo());
        System.out.println(scanQrCodePayResponseBo.getQrcode_url());
        System.out.println(scanQrCodePayResponseBo.toString());
    }

    @Test
    public void queryPayStatus(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        String outTradeNo= "101181017192442218000000";
        Long paymentItemId=155l;
        SyncPayStatusResponseBo scanQrCodePayResponseBo=YiPayApi.syncPayStatus(paymentParamBo,outTradeNo,null);
        System.out.println("Out trade no is:"+scanQrCodePayResponseBo.getOut_trade_no());
        System.out.println(scanQrCodePayResponseBo.toString());
    }

    @Test
    public void refund(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        String outTradeNo= "101181017192442218000000";
        String outRefundId="101181017192442218000000";
        YiPayRefundResponseBo refundResponseBo=YiPayApi.refund(paymentParamBo,1,outTradeNo,null,outRefundId);
        System.out.println("Refund id is:"+refundResponseBo.getHaipay_refund_id());
        System.out.println("Out refund no is:"+outRefundId);
        System.out.println(refundResponseBo.toString());
    }

    @Test
    public void queryRefundStatus(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        String outRefundId="b14572ba2a744dc7a32ddf1537ea0e99";
        QueryYiPayRefundStatusResponseBo refundResponseBo=YiPayApi.queryRefundStatus(paymentParamBo,null,"201811041739457380450950");
        System.out.println("Refund id is: 20180920002233621701404062480");
        System.out.println("Out refund no is:"+outRefundId);
        System.out.println(refundResponseBo.toString());
    }

    @Test
    public void microPay(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid("hf163356826045OA");
        String nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA = "MgAtKIRKPMMbbfycOw5b87U6NP024kWA";
        paymentParamBo.setAppsecret(nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA);
        paymentParamBo.setContextPath(callbackContextPath);
        PrePayBo prePayBo=new PrePayBo();
        prePayBo.setOutTradeNo( "10118101711442f18000000");
        prePayBo.setTradeAmountInCent(1);
        prePayBo.setNeedYiPay(true);
//        prePayBo.setAuthCode("asssss");
//        prePayBo.setAliPay();
        prePayBo.setWechatPay();
        prePayBo.setWechatAppid("wx22d9607fa73e9364");
        prePayBo.setWechatOpenId("oTf-e4g_nqsOuXwBBZnt1eenqUWE");

        MicroAppPayResponseBo microAppPayResponseBo=YiPayApi.microAppPay(paymentParamBo,prePayBo,"182.139.217.242");
        System.out.println(microAppPayResponseBo.toString());
    }

}
