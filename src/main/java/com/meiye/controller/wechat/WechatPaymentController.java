package com.meiye.controller.wechat;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.MicroPayRequestBo;
import com.meiye.bo.accounting.MicroPayRequestContentBo;
import com.meiye.bo.pay.MicroAppPayResponseBo;
import com.meiye.bo.pay.StorePaymentParamBo;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.controller.payment.AbstractPayController;
import com.meiye.exception.BusinessException;
import com.meiye.util.YiPayApi;
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

    @PostMapping("/microPay")
    public PosApiResult microPay(@RequestBody MicroPayRequestBo accountingBo, HttpServletRequest request){
        StorePaymentParamBo storePaymentParamBo=payService.getStorePaymentParamBo(accountingBo.getShopId());
        MicroPayRequestContentBo paymentContent=accountingBo.getContent();
        MicroAppPayResponseBo microAppPayResponseBo=YiPayApi.microAppPay(storePaymentParamBo,paymentContent.getTotal_amount(),paymentContent.getOut_trade_no(),paymentContent.getSub_appid(),paymentContent.getSub_openid(),paymentContent.getSpbill_create_ip(),paymentContent.getPayment_item_id());
        if(microAppPayResponseBo.isSuccess())
            return PosApiResult.sucess(microAppPayResponseBo);
        else
            return PosApiResult.error(microAppPayResponseBo,1003,"调用小程序支付失败");
    }
}
