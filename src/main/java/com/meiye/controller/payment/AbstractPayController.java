package com.meiye.controller.payment;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.pay.*;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.bo.trade.TradeBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.setting.Tables;
import com.meiye.model.trade.Trade;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.util.MeiYeInternalApi;
import com.meiye.util.ObjectUtil;
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
    protected PayService payService;

    @Autowired
    protected OrderService orderService;



    protected PosApiResult pay(AccountingBo accountingBo, String payRequestType){
        try {
            Map<String,Object> payResult=new HashMap<>();
            PrePayBo payReturnBo =payService.savePaymentData(accountingBo,payRequestType);
            Long orderId=accountingBo.getContent().getTrade().getId();
            //开始调用核销程序
            WriteOffResultBo resultBo= MeiYeInternalApi.writeOff(orderId,accountingBo.getBrandId(),accountingBo.getShopId());
            if(resultBo.isSuccess()){
                try {
                    if(payReturnBo.isBanlancePay()){
                        Long customerId=orderService.getCustomerIdByType(orderId,3);
                        TradeBo trade=orderService.getTradeByTradeId(orderId);
                        InternalApiResult internalApiResult=MeiYeInternalApi.expense(customerId,orderId,payReturnBo.getPaymentItemId(),payReturnBo.getTradeAmount(),trade.getShopIdenty(),trade.getBrandIdenty(),trade.getCreatorId(),trade.getCreatorName());
                        if(!internalApiResult.isSuccess()){
                            throw new BusinessException("余额扣款失败", ResetApiResult.STATUS_ERROR,1003);
                        }
                    }

                    if (payReturnBo.isNeedYiPay()) {
                        try {
                            StorePaymentParamBo storePaymentParamBo = payService.getStorePaymentParamBo(accountingBo.getShopId());
                            //如果需要调用翼支付，则发起翼支付的调用
                            if ("ScanPay".equalsIgnoreCase(payRequestType)) {
                                ScanPayResponseBo scanPayResponseBo = YiPayApi.scanPay(storePaymentParamBo, payReturnBo);
                                if (scanPayResponseBo.isPaySuccess()) {
//                                    Thread.sleep(10000);
                                    payService.paySuccess(payReturnBo.getOutTradeNo(), scanPayResponseBo.getTrade_id());
                                    payService.afterPaySucess(orderId);
                                    payResult.putAll(getOrderPaymentData(orderId));
                                } else
                                    throw new BusinessException("扫码支付失败,请手工同步订单状态", ResetApiResult.STATUS_ERROR, 1010);
                            } else if ("ScanQrPay".equalsIgnoreCase(payRequestType)) {
                                ScanQrCodePayResponseBo scanQrCodePayResponseBo = YiPayApi.getQrCodeForPay(storePaymentParamBo, payReturnBo);
                                if (scanQrCodePayResponseBo.isSuccess())
                                    payResult.put("qrcodeUrl", scanQrCodePayResponseBo.getQrcode_url());
                                else
                                    throw new BusinessException("获取支付二维码失败", ResetApiResult.STATUS_ERROR, 1003);
                            }
                        }catch (Exception exp){
                            logger.info("翼支付调用失败：",exp);
                            throw new BusinessException("翼支付调用失败.", ResetApiResult.STATUS_ERROR, ResetApiResult.YIPAY_API_CALL_FAILED);
                        }
                    } else {
                        //不需要翼支付，则开始修改订单状态为支付完成
                        payService.paySuccess(payReturnBo.getOutTradeNo(),null);
                        payService.afterPaySucess(orderId);
                        payResult.putAll(getOrderPaymentData(orderId));
                    }
                }catch (BusinessException exp){
                    logger.error("支付失败",exp);
                    String message="支付失败:"+exp.getMessage();
                    BusinessException exception=exp;
                    try {
                        WriteOffResultBo returnPrivilege = MeiYeInternalApi.returnPrivilege(orderId, accountingBo.getBrandId(), accountingBo.getShopId());

                        if(!ObjectUtil.equals(exp.getStatusCode(),ResetApiResult.YIPAY_API_CALL_FAILED)) {
                            if (returnPrivilege.isSuccess())
                                exception = new BusinessException(message, ResetApiResult.STATUS_ERROR, exp.getStatusCode());
                            else
                                exception = new BusinessException(message + "，调用反核销程序失败：" + returnPrivilege.getMsg(), ResetApiResult.STATUS_ERROR, exp.getStatusCode());
                        }
                    }catch (Exception e){
                        if(!ObjectUtil.equals(exp.getStatusCode(),ResetApiResult.YIPAY_API_CALL_FAILED)) {
                            exception = new BusinessException(message + "，调用反核销程序失败：" + e.getMessage(), ResetApiResult.STATUS_ERROR, exp.getStatusCode());
                        }
                    }
                    throw exception;
                } catch (Exception exp){
                    logger.error("支付失败",exp);
                    WriteOffResultBo returnPrivilege=MeiYeInternalApi.returnPrivilege(orderId,accountingBo.getBrandId(),accountingBo.getShopId());
                    if(returnPrivilege.isSuccess())
                        throw new BusinessException("支付失败", ResetApiResult.STATUS_ERROR,1003);
                    else
                        throw new BusinessException("支付失败，调用反核销程序失败："+returnPrivilege.getMsg(),ResetApiResult.STATUS_ERROR,1003);
                }
            }else{
                //处理错误逻辑
//                if("1002".equals(resultBo.getState())||"1003".equals(resultBo.getState()))
                MeiYeInternalApi.returnPrivilege(orderId,accountingBo.getBrandId(),accountingBo.getShopId());//核销失败,调用反核销，这次反核销不用考虑反核销的结果
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
