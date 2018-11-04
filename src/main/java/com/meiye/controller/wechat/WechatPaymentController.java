package com.meiye.controller.wechat;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.MicroPayRequestBo;
import com.meiye.bo.accounting.MicroPayRequestContentBo;
import com.meiye.bo.pay.MicroAppPayResponseBo;
import com.meiye.bo.pay.PrePayBo;
import com.meiye.bo.pay.StorePaymentParamBo;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.controller.payment.AbstractPayController;
import com.meiye.controller.posApi.PaymentController;
import com.meiye.exception.BusinessException;
import com.meiye.util.YiPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/10/6 0006.
 */
@RestController
@RequestMapping(value = "/weichat/api/pay",produces="application/json;charset=UTF-8")
public class WechatPaymentController extends AbstractPayController {
    Logger logger= LoggerFactory.getLogger(PaymentController.class);

    @PostMapping("/microPay")
    public PosApiResult microPay(@RequestBody MicroPayRequestBo accountingBo, HttpServletRequest request){
        try {
            StorePaymentParamBo storePaymentParamBo = payService.getStorePaymentParamBo(accountingBo.getShopId());
            MicroPayRequestContentBo paymentContent = accountingBo.getContent();
            PrePayBo prePayBo=new PrePayBo();
            prePayBo.setTradeAmountInCent(paymentContent.getTotal_amount());
            prePayBo.setOutTradeNo(paymentContent.getOut_trade_no());
            prePayBo.setWechatAppid(ObjectUtils.isEmpty(paymentContent.getSub_appid())?payService.getStoreWechatAppId(accountingBo.getShopId()):paymentContent.getSub_appid());
            prePayBo.setWechatOpenId(paymentContent.getSub_openid());
            prePayBo.setPaymentItemId(paymentContent.getPayment_item_id());
            prePayBo.setPayRequestType("MicroPay");
            MicroAppPayResponseBo microAppPayResponseBo = YiPayApi.microAppPay(storePaymentParamBo,prePayBo,paymentContent.getSpbill_create_ip());
            if (microAppPayResponseBo.isSuccess())
                return PosApiResult.sucess(microAppPayResponseBo);
            else
                return PosApiResult.error(microAppPayResponseBo, 1003, "调用小程序支付失败");
        }catch (BusinessException exp){
            logger.error("微信小程序支付失败",exp);
            return PosApiResult.error(null, 1003, "调用小程序支付失败:"+exp.getMessage());
        }catch (Exception exp){
            logger.error("微信小程序支付失败",exp);
            return PosApiResult.error(null, 1003, "调用小程序支付失败");
        }
    }
}
