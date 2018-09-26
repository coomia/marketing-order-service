package com.meiye.controller.payment;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.pay.*;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.controller.posApi.PaymentController;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.setting.Tables;
import com.meiye.model.trade.Trade;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.util.MeiYeInternalApi;
import com.meiye.util.YiPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@RestController
public class AbstractPayController {
    Logger logger= LoggerFactory.getLogger(AbstractPayController.class);
    @Autowired
    PayService payService;

    @Autowired
    OrderService orderService;



    protected PosApiResult pay(AccountingBo accountingBo, String payRequestType, HttpServletRequest request){
        try {
            Map<String,Object> payResult=new HashMap<>();
            payService.savePaymentData(accountingBo);
            Long orderId=accountingBo.getContent().getTrade().getId();
            //开始调用核销程序
            WriteOffResultBo resultBo= MeiYeInternalApi.writeOff(orderId,accountingBo.getBrandId(),accountingBo.getShopId());
            if(resultBo.isSuccess()){
                try {
                    PrePayReturnBo payReturnBo = payService.prePay(accountingBo, payRequestType);
                    if (payReturnBo.isNeedYiPay()) {
                        StorePaymentParamBo storePaymentParamBo=payService.getStorePaymentParamBo(accountingBo.getShopId());
                        //如果需要调用翼支付，则发起翼支付的调用
                        if("MicroPay".equalsIgnoreCase(payRequestType)){
                            MicroAppPayResponseBo microAppPayResponseBo=YiPayApi.microAppPay(storePaymentParamBo,payReturnBo.getTradeAmout(),payReturnBo.getOutTradeNo(),payReturnBo.getWechatAppid(),payReturnBo.getWechatOpenId(),request.getRemoteHost());
                            if(microAppPayResponseBo.isSuccess())
                                payResult.put("wechatPayParams",microAppPayResponseBo);
                            else
                                throw new BusinessException("调用小程序支付失败",ResetApiResult.STATUS_ERROR,1003);
                        }else if("ScanPay".equalsIgnoreCase(payRequestType)){
                            ScanPayResponseBo scanPayResponseBo=YiPayApi.scanPay(storePaymentParamBo,payReturnBo.getTradeAmout(),payReturnBo.getAuthCode(),payReturnBo.getOutTradeNo(),payReturnBo.getPayType());
                            if(scanPayResponseBo.isPaySuccess()){
                                payService.paySuccess(orderId);
                                payResult.putAll(getOrderPaymentData(orderId));
                            }else
                                throw new BusinessException("扫码支付失败,请手工同步订单状态",ResetApiResult.STATUS_ERROR,1003);
                        }else if("ScanQrPay".equalsIgnoreCase(payRequestType)){
                            ScanQrCodePayResponseBo scanQrCodePayResponseBo=YiPayApi.getQrCodeForPay(storePaymentParamBo,payReturnBo.getOutTradeNo(),payReturnBo.getTradeAmout());
                            if(scanQrCodePayResponseBo.isSuccess())
                                payResult.put("qrcodeUrl",scanQrCodePayResponseBo.getQrcode_url());
                            else
                                throw new BusinessException("获取支付二维码失败",ResetApiResult.STATUS_ERROR,1003);
                        }
                    } else {
                        //不需要翼支付，则开始修改订单状态为支付完成
                        payService.paySuccess(orderId);
                        payResult.putAll(getOrderPaymentData(orderId));
                    }
                }catch (Exception exp){
                    logger.error("支付失败",exp);
                    WriteOffResultBo returnPrivilege=MeiYeInternalApi.returnPrivilege(orderId,accountingBo.getBrandId(),accountingBo.getShopId());
                    throw new BusinessException("支付失败", ResetApiResult.STATUS_ERROR,1003);
                }
            }else{
                //处理错误逻辑
                if("1002".equals(resultBo.getState())||"1003".equals(resultBo.getState()))
                    return PosApiResult.error(null,5002,resultBo.getMsg());
            }
            return PosApiResult.sucess(payResult);
        }catch (BusinessException exp){
            logger.error("收银失败",exp);
            return   PosApiResult.error(null,exp.getStatusCode(),exp.getMessage());
        }catch (Exception exp){
            logger.error("收银失败",exp);
            return   PosApiResult.error(null,"收银失败:"+exp.getMessage());
        }
    }

    public Map<String,Object> getOrderPaymentData(Long tradeId){
        Map<String,Object> result=new HashMap<>();
        OrderResponseDto orderResponseDto=orderService.getOrderResponse(tradeId,false);
        List<Trade> trades=new ArrayList<>();
        if(!ObjectUtils.isEmpty(orderResponseDto.getTrade()))
            trades.add(orderResponseDto.getTrade());
        result.put("trades",trades);
        result.put("tradeTables",orderResponseDto.getTradeTables());
        result.put("tables",ObjectUtils.isEmpty(orderResponseDto.getTables())?new ArrayList<Tables>():orderResponseDto.getTables());
//        result.put("tradeItems",orderResponseDto.getTradeItems());
//        result.put("tradeCustomers",orderResponseDto.getTradeCustomers());
//        result.put("tradePrivileges",orderResponseDto.getTradePrivileges());
//        result.put("tradeItemProperties",orderResponseDto.getTradeItemProperties());
//        result.put("tradeUsers",orderResponseDto.getTradeUsers());
//        result.put("customerCardTimes",orderResponseDto.getCustomerCardTimes());

        List<Payment> payments=payService.findPaymentsByTradeId(tradeId,false);
        result.put("payments",payments);
        if(!ObjectUtils.isEmpty(payments)) {
            List<Long> paymentIds = payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
            List<PaymentItem> paymentItems = payService.findPaymentItemsByPamentId(paymentIds, false);
            result.put("paymentItems", paymentItems);
            if(!ObjectUtils.isEmpty(paymentItems)) {
                List<Long> paymentItemIds = paymentItems.stream().map(PaymentItem::getId).distinct().collect(Collectors.toList());
                List<PaymentItemExtra> paymentItemExtras = payService.findPaymentItemExtraByPamentItemId(paymentItemIds, false);
                result.put("paymentItemExtras", paymentItemExtras);
            }else
                result.put("paymentItemExtras", new ArrayList<>());
        }else{
            result.put("paymentItemExtras", new ArrayList<>());
            result.put("paymentItems", new ArrayList<>());
        }
        return result;
    }


}
