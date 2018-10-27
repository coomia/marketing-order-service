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
    private final String callbackContextPath="http://118.113.202.4:7090/MeiYe";


    private final String appid="hf19102035480OVA";
    private final String appsecret="nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA";
    @Test
    public void testScanPay(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        ScanPayResponseBo scanPayResponseBo=YiPayApi.scanPay(paymentParamBo,1,"sdssdsdsd","11111111112222332","0",1l);
        System.out.println(scanPayResponseBo.toString());
    }


    @Test
    public void testScanQrCodePay(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        String outTradeNo= UUIDUtil.randomUUID();
        ScanQrCodePayResponseBo scanQrCodePayResponseBo=YiPayApi.getQrCodeForPay(paymentParamBo,outTradeNo,1,1l);
        System.out.println("Out trade no is:"+outTradeNo);
        System.out.println(scanQrCodePayResponseBo.getQrcode_url());
        System.out.println(scanQrCodePayResponseBo.toString());
    }

    @Test
    public void queryPayStatus(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        SyncPayStatusResponseBo scanQrCodePayResponseBo=YiPayApi.syncPayStatus(paymentParamBo,"8679d86c46bd4320ae7bc5d10d6cab84",null);
        System.out.println("Out trade no is:"+scanQrCodePayResponseBo.getOut_trade_no());
        System.out.println(scanQrCodePayResponseBo.toString());
    }

    @Test
    public void refund(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid(appid);
        paymentParamBo.setAppsecret(appsecret);
        paymentParamBo.setContextPath(callbackContextPath);
        String outRefundId=UUIDUtil.randomUUID();
        YiPayRefundResponseBo refundResponseBo=YiPayApi.refund(paymentParamBo,1,"8679d86c46bd4320ae7bc5d10d6cab84",null,outRefundId);
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
        String outRefundId="bd7fcdb704e44b859515be3e16bb440f";
        QueryYiPayRefundStatusResponseBo refundResponseBo=YiPayApi.queryRefundStatus(paymentParamBo,outRefundId,null);
        System.out.println("Refund id is: 20180920002233621701404062480");
        System.out.println("Out refund no is:"+outRefundId);
        System.out.println(refundResponseBo.toString());
    }

    @Test
    public void microPay(){
        StorePaymentParamBo paymentParamBo=new StorePaymentParamBo();
        paymentParamBo.setAppid("hf19102035480OVA");
        paymentParamBo.setAppsecret("nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA");
        paymentParamBo.setContextPath(callbackContextPath);
        MicroAppPayResponseBo microAppPayResponseBo=YiPayApi.microAppPay(paymentParamBo,1,"bd7fcdb704e44b8595s5be3e16443f","wx8ea448dd310b9504","owH5N5aO7aYdMJ0zfT4HlPF-pbsE","118.112.52.34",1l);
        System.out.println(microAppPayResponseBo.toString());
    }

}
