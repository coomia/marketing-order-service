package com.meiye.service.pay.impl;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.pay.*;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.TradeBo;
import com.meiye.bo.trade.TradeItemBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.trade.Trade;
import com.meiye.model.trade.TradeItem;
import com.meiye.repository.pay.PaymentItemExtraRepository;
import com.meiye.repository.pay.PaymentItemRepository;
import com.meiye.repository.pay.PaymentRepository;
import com.meiye.repository.trade.TradeItemRepository;
import com.meiye.repository.trade.TradeRepository;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.system.util.WebUtil;
import com.meiye.util.MeiYeInternalApi;
import com.meiye.util.UUIDUtil;
import com.meiye.util.YiPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Service
public class PayServiceImpl implements PayService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    OrderService orderService;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentItemRepository paymentItemRepository;

    @Autowired
    PaymentItemExtraRepository paymentItemExtraRepository;

    @Autowired
    TradeItemRepository tradeItemRepository;

    @Override
    public String getAppSercet(Integer storeId){
        return "test";
    }


    //TODO 验证支付
    public void prePay(){

    }

    @Override
    @Transactional
    public void yipaySuccess(String tradeNo, String yiPayTradeNo, Long paymentId){
        Trade trade=orderService.getTradeByTradeNo(tradeNo);
        if(trade==null)
            throw new BusinessException("交易记录不存在.");
        this.paySuccess(trade.getId(),paymentId,yiPayTradeNo);
    }

    @Override
    @Transactional
    public void paySuccess(Long tradeId, Long paymentItemId, String yiPayTradeNo){
        Optional<Trade> tradeOption=tradeRepository.findById(tradeId);
        if(!tradeOption.isPresent())
            throw new BusinessException("交易记录不存在.");
        Trade trade=tradeOption.get();
        if(!trade.getTradePayStatus().equals(1)&&!trade.getTradePayStatus().equals(2))
            return;
        trade.setTradePayStatus(3);
        trade.setTradeStatus(4);
        Timestamp now=new Timestamp(Calendar.getInstance().getTimeInMillis());
        trade.setServerUpdateTime(now);
        tradeRepository.save(trade);
        List<Payment> payments=this.findPaymentsByTradeId(trade.getId(),false);
        for(Payment payment:payments){
            payment.setIsPaid(1);
            payment.setServerUpdateTime(now);
        }
        paymentRepository.saveAll(payments);

        List<Long> paymentIds=payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
        List<PaymentItem> paymentItems=this.findPaymentItemsByPamentId(paymentIds,false);
        for(PaymentItem paymentItem:paymentItems){
            paymentItem.setPayStatus(3);
            paymentItem.setServerUpdateTime(now);
        }
        paymentItemRepository.saveAll(paymentItems);

        if(paymentItemId!=null) {
            Optional<PaymentItem> paymentItemOptional = paymentItemRepository.findById(paymentItemId);
            if (paymentItemOptional.isPresent()) {
                PaymentItem paymentItem = paymentItemOptional.get();
                paymentItem.setPayStatus(3);
                paymentItem.setServerUpdateTime(now);
                paymentItemRepository.saveAll(paymentItems);
            }
        }

        //调用过翼支付，并且获取到了翼支付的tradeId
        if (!ObjectUtils.isEmpty(paymentItemId) && !ObjectUtils.isEmpty(yiPayTradeNo)) {
            List<PaymentItemExtra> paymentItemExtras=paymentItemExtraRepository.findByPaymentItemIdAndStatusFlag(paymentItemId,1);
            if(!ObjectUtils.isEmpty(paymentItemExtras)){
                for(PaymentItemExtra paymentItemExtra:paymentItemExtras){
                    paymentItemExtra.setPayTranNo(yiPayTradeNo);
                    paymentItemExtra.setPayCallbackTime(now);
                    paymentItemExtra.setStatusFlag(1);
                    paymentItemExtra.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
                    paymentItemExtra.setServerCreateTime(new Timestamp(System.currentTimeMillis()));
                    paymentItemExtra.setUpdatorId(trade.getUpdatorId());
                    paymentItemExtra.setUpdatorName(trade.getUpdatorName());
                }
                paymentItemExtraRepository.saveAll(paymentItemExtras);
            }else {
                PaymentItemExtra paymentItemExtra = new PaymentItemExtra();
                paymentItemExtra.setUuid(UUIDUtil.randomUUID());
                paymentItemExtra.setPaymentItemId(paymentItemId);
                paymentItemExtra.setPayTranNo(yiPayTradeNo);
                paymentItemExtra.setPayCallbackTime(now);
                paymentItemExtra.setShopIdenty(trade.getShopIdenty());
                paymentItemExtra.setBrandIdenty(trade.getBrandIdenty());
                paymentItemExtra.setDeviceIdenty(trade.getDeviceIdenty());
                paymentItemExtra.setStatusFlag(1);
                paymentItemExtra.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
                paymentItemExtra.setServerCreateTime(new Timestamp(System.currentTimeMillis()));
                paymentItemExtra.setCreatorId(trade.getCreatorId());
                paymentItemExtra.setCreatorName(trade.getCreatorName());
                paymentItemExtra.setUpdatorId(trade.getUpdatorId());
                paymentItemExtra.setUpdatorName(trade.getUpdatorName());
                paymentItemExtraRepository.save(paymentItemExtra);
            }
        }
    }


    @Transactional
    @Override
    public String returnPayment(Long tradeId){
        String message=null;
        Trade returnTrade = tradeRepository.findById(tradeId).get();
        try {
            Trade oriTrade = tradeRepository.findById(returnTrade.getRelateTradeId()).get();
            List<Payment> payments = this.findPaymentsByTradeId(tradeId, false);
            WriteOffResultBo resultBo=MeiYeInternalApi.returnPrivilege(tradeId, WebUtil.getCurrentStoreId(), WebUtil.getCurrentBrandId());
            if(!resultBo.isSuccess())
                message="反核销重新和余额退回失败：" + resultBo.getMsg();
            Integer returnAmt = 0;
            List<PaymentItem> yiPaymentItems = new ArrayList<>();
            if (!ObjectUtils.isEmpty(payments)) {
                for (Payment payment : payments) {
                    if (payment.getIsPaid() == 1) {
                        List<PaymentItem> paymentItems = paymentItemRepository.findByPaymentIdAndStatusFlag(payment.getId(), 1);
                        if (!ObjectUtils.isEmpty(paymentItems)) {
                            for (PaymentItem paymentItem : paymentItems) {
                                if (paymentItem.getPayStatus() == 4) {
                                    if (paymentItem.getPayModeId() == 1) {
                                        InternalApiResult internalApiResult = MeiYeInternalApi.refund(orderService.getCustomerIdByType(tradeId, 3), tradeId, paymentItem.getId(), paymentItem.getUsefulAmount(), WebUtil.getCurrentStoreId(), WebUtil.getCurrentBrandId(), paymentItem.getCreatorId(), paymentItem.getCreatorName());
                                        if (!internalApiResult.isSuccess())
                                            message="余额退回失败:"+internalApiResult.getMsg();
                                    } else if (paymentItem.getPayModeId() == 4 || paymentItem.getPayModeId() == 5) {
                                        yiPaymentItems.add(paymentItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!ObjectUtils.isEmpty(yiPaymentItems)) {
                returnAmt = new Double(yiPaymentItems.get(0).getUsefulAmount() * 100).intValue();
                YiPayRefundResponseBo yiPayRefundResponseBo = YiPayApi.refund(getStorePaymentParamBo(WebUtil.getCurrentStoreId()), returnAmt, oriTrade.getTradeNo(), null, returnTrade.getTradeNo());
                if (yiPayRefundResponseBo == null || !yiPayRefundResponseBo.isSuccess()) {
                    message="翼支付退款失败";
                } else {
                    PaymentItemExtra paymentItemExtra = new PaymentItemExtra();
                    paymentItemExtra.setUuid(UUIDUtil.randomUUID());
                    paymentItemExtra.setPaymentItemId(yiPaymentItems.get(0).getId());
                    paymentItemExtra.setShopIdenty(oriTrade.getShopIdenty());
                    paymentItemExtra.setBrandIdenty(oriTrade.getBrandIdenty());
                    paymentItemExtra.setDeviceIdenty(oriTrade.getDeviceIdenty());
                    paymentItemExtra.setStatusFlag(1);
                    paymentItemExtra.setRefundCallbackTime(new Timestamp(System.currentTimeMillis()));
                    paymentItemExtra.setRefundTradeNo(yiPayRefundResponseBo.getHaipay_refund_id());
                    paymentItemExtra.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
                    paymentItemExtra.setServerCreateTime(new Timestamp(System.currentTimeMillis()));
                    paymentItemExtra.setCreatorId(oriTrade.getCreatorId());
                    paymentItemExtra.setCreatorName(oriTrade.getCreatorName());
                    paymentItemExtra.setUpdatorId(oriTrade.getUpdatorId());
                    paymentItemExtra.setUpdatorName(oriTrade.getUpdatorName());
                    paymentItemExtraRepository.save(paymentItemExtra);
                }
            }else{
                refundSuccessful(tradeId);
            }
        }catch (Exception exp){
            logger.info("订单("+tradeId+")退款失败",exp);
            message="订单退款失败";
        }
        if(!ObjectUtils.isEmpty(message))
            refundFailed(tradeId);
        return message;
    }


    @Transactional
    @Override
    public void refundSuccessful(Long tradeId){
        Trade trade=tradeRepository.findById(tradeId).get();
        if(trade.getTradePayStatus()==4){
            trade.setTradePayStatus(5);
            trade.setTradeStatus(5);
            List<Payment> payments = this.findPaymentsByTradeId(tradeId, false);
            if (!ObjectUtils.isEmpty(payments)) {
                for (Payment payment : payments) {
                    if (payment.getIsPaid() == 1) {
                        List<PaymentItem> paymentItems = paymentItemRepository.findByPaymentIdAndStatusFlag(payment.getId(), 1);
                        if (!ObjectUtils.isEmpty(paymentItems)) {
                            for (PaymentItem paymentItem : paymentItems) {
                                paymentItem.setIsRefund(1);
                                paymentItem.setPayStatus(5);
                            }
                        }
                        paymentItemRepository.saveAll(paymentItems);
                    }
                }
            }
            tradeRepository.save(trade);
        }
        //次卡退货不用考虑，不会发生退货
        MeiYeInternalApi.subtractCommission(trade.getId(),trade.getBrandIdenty(),trade.getShopIdenty());
    }

    @Transactional
    @Override
    public void refundFailed(Long tradeId){
        Trade trade=tradeRepository.findById(tradeId).get();
        if(trade.getTradePayStatus()==4){
            trade.setTradePayStatus(6);
            List<Payment> payments = this.findPaymentsByTradeId(tradeId, false);
            if (!ObjectUtils.isEmpty(payments)) {
                for (Payment payment : payments) {
                    if (payment.getIsPaid() == 1) {
                        List<PaymentItem> paymentItems = paymentItemRepository.findByPaymentIdAndStatusFlag(payment.getId(), 1);
                        if (!ObjectUtils.isEmpty(paymentItems)) {
                            for (PaymentItem paymentItem : paymentItems) {
                                paymentItem.setIsRefund(2);
                                paymentItem.setPayStatus(6);
                            }
                        }
                        paymentItemRepository.saveAll(paymentItems);
                    }
                }
            }
            tradeRepository.save(trade);
        }
    }


    @Override
    public void afterPaySucess(Long tradeId){
        Optional<Trade> tradeOption=tradeRepository.findById(tradeId);
        if(!tradeOption.isPresent())
            throw new BusinessException("交易记录不存在.");
        Trade trade=tradeOption.get();
        if(trade.getBusinessType()==2||trade.getBusinessType()==3) {
            Long customerId = orderService.getCustomerIdByType(tradeId, 3);
            Double payedAmount=null;
            List<Payment> payments=this.findPaymentsByTradeId(trade.getId(),true);
            List<Long> paymentIds=payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
            List<PaymentItem> paymentItems=this.findPaymentItemsByPamentId(paymentIds,true);
            Long paymentId=null;
            for(PaymentItem paymentItem:paymentItems){
                if(paymentItem.getPayStatus()==3) {
                    payedAmount = payedAmount == null ? paymentItem.getUsefulAmount() : payedAmount + paymentItem.getUsefulAmount();
                    paymentId=paymentItem.getId();
                }
            }
            if (trade.getBusinessType() == 2) {
                //储值
                InternalApiResult internalApiResult=MeiYeInternalApi.recharge(customerId, tradeId, paymentId,new BigDecimal(payedAmount),trade.getShopIdenty(),trade.getBrandIdenty(),trade.getCreatorId(),trade.getCreatorName());
                if(!internalApiResult.isSuccess())
                    throw new BusinessException("充值失败:"+internalApiResult.getMsg());
            } else if (trade.getBusinessType() == 3) {
                //次卡
                TradeBo tradeBo=trade.copyTo(TradeBo.class);
                List<TradeItem> tradeItems=tradeItemRepository.findAllByTradeIdAndStatusFlag(tradeId,1);
                if(!ObjectUtils.isEmpty(tradeItems)){
                    List<TradeItemBo> tradeItemBos=new ArrayList<>();
                    for(TradeItem tradeItem:tradeItems){
                        tradeItemBos.add(tradeItem.copyTo(TradeItemBo.class));
                    }
                    tradeBo.setTradeItems(tradeItemBos);
                }
                InternalApiResult internalApiResult=MeiYeInternalApi.buyCardTimes(customerId,tradeBo);
                if(!internalApiResult.isSuccess())
                    throw new BusinessException("购买次卡失败:"+internalApiResult.getMsg());
            }
        }
        //增加消费提成
        if(trade.getBusinessType() ==1)
            MeiYeInternalApi.addCommission(trade.getId(),trade.getBrandIdenty(),trade.getShopIdenty());
    }


    @Override
    @Transactional
    public void savePaymentData(AccountingBo accountingBo){
        try {

            Optional<Trade> tradeOption=tradeRepository.findById(accountingBo.getContent().getTrade().getId());
            if(!tradeOption.isPresent())
                throw new BusinessException("订单不存在。", ResetApiResult.STATUS_ERROR,1001);
            Trade trade=tradeOption.get();
            if(!trade.getServerUpdateTime().equals(accountingBo.getContent().getTrade().getServerUpdateTime()))
                throw new BusinessException("订单已经被其他人处理。", ResetApiResult.STATUS_ERROR,5004);
            //删除已存在的支付记录
            paymentRepository.disableExistPaymentByRelateId(trade.getId());

            //保存新的payment记录
            PaymentBo paymentBo=accountingBo.getContent().getPayment();
            paymentBo.setRelateId(trade.getId());
            paymentBo.setRelateUuid(trade.getUuid());
            paymentBo.setBizDate(new Date());
            paymentBo.setIsPaid(2);
            paymentBo.setDeviceIdenty(accountingBo.getDeviceId());
            Payment payment=paymentBo.copyTo(Payment.class);
            paymentRepository.save(payment);

            for(PaymentItemBo paymentItemBo:paymentBo.getPaymentItems()){
                paymentItemBo.setPaymentId(payment.getId());
                paymentItemBo.setDeviceIdenty(accountingBo.getDeviceId());
                paymentItemBo.setPaymentUuid(payment.getUuid());
                PaymentItem paymentItem=paymentItemBo.copyTo(PaymentItem.class);
                paymentItemRepository.save(paymentItem);
                if(!ObjectUtils.isEmpty(paymentItemBo.getPaymentItemExtra())) {
                    for (PaymentItemExtraBo paymentItemExtraBo : paymentItemBo.getPaymentItemExtra()) {
                        paymentItemExtraBo.setPaymentItemId(paymentItem.getId());
                        paymentItemExtraBo.setDeviceIdenty(accountingBo.getDeviceId());
                        PaymentItemExtra paymentItemExtra = paymentItemExtraBo.copyTo(PaymentItemExtra.class);
                        paymentItemExtraRepository.save(paymentItemExtra);
                    }
                }
            }
        }catch (BusinessException exp){
            logger.error("Save payment data face error:",exp);
            throw exp;
        } catch (Exception exp){
            logger.error("Save payment data face error:",exp);
            throw new BusinessException("保存payment数据失败.",ResetApiResult.STATUS_ERROR,1001);
        }

    }

    @Override
    public List<Payment> findPaymentsByTradeId(Long relateId, boolean includeInactive){
        List<Payment> payments=null;
        if(!includeInactive)
            payments=paymentRepository.findByRelateIdAndStatusFlag(relateId,1);
        else
            payments=paymentRepository.findByRelateId(relateId);
        return payments;
    }

    @Override
    public List<PaymentItem> findPaymentItemsByPamentId(List<Long> paymentIds, boolean includeInactive){
        List<PaymentItem> paymentItems=null;
        if(!includeInactive)
            paymentItems=paymentItemRepository.findByPaymentIdInAndStatusFlag(paymentIds,1);
        else
            paymentItems=paymentItemRepository.findByPaymentIdIn(paymentIds);
        return paymentItems;
    }


    @Override
    public List<PaymentItemExtra> findPaymentItemExtraByPamentItemId(List<Long> paymentItemIds, boolean includeInactive){
        List<PaymentItemExtra> paymentItems=null;
        if(!includeInactive)
            paymentItems=paymentItemExtraRepository.findByPaymentItemIdInAndStatusFlag(paymentItemIds,1);
        else
            paymentItems=paymentItemExtraRepository.findByPaymentItemIdIn(paymentItemIds);
        return paymentItems;
    }

    @Override
    @Transactional
    public PrePayReturnBo prePay(AccountingBo accountingBo, String payRequestType){
        Optional<Trade> tradeOptional= tradeRepository.findById(accountingBo.getContent().getTrade().getId());


        if(!tradeOptional.isPresent())
            throw new BusinessException("订单不存在。", ResetApiResult.STATUS_ERROR,1001);
        Trade trade=tradeOptional.get();
        if(!trade.getServerUpdateTime().equals(accountingBo.getContent().getTrade().getServerUpdateTime()))
            throw new BusinessException("订单已经被其他人处理。", ResetApiResult.STATUS_ERROR,5004);
        PrePayReturnBo prePayReturnBo=new PrePayReturnBo();
        prePayReturnBo.setPayRequestType(payRequestType);
        prePayReturnBo.setOutTradeNo(trade.getTradeNo());

        List<Payment> payments=this.findPaymentsByTradeId(accountingBo.getContent().getTrade().getId(),false);
        List<Long> paymentIds=payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
        List<PaymentItem> paymentItems=this.findPaymentItemsByPamentId(paymentIds,false);
        List<PaymentItem> wechatPayItems=new ArrayList<>();
        List<PaymentItem> alipayItems=new ArrayList<>();
        List<PaymentItem> balancePayItems=new ArrayList<>();
        for(PaymentItem paymentItem:paymentItems){
            if(paymentItem.getPayModeId().equals(4l)){
                //4 微信支付，5支付宝支付
                wechatPayItems.add(paymentItem);
            }else if(paymentItem.getPayModeId().equals(5l)){
                alipayItems.add(paymentItem);
            }else if(paymentItem.getPayModeId().equals(1l)){
                balancePayItems.add(paymentItem);
            }
        }
        if(!ObjectUtils.isEmpty(wechatPayItems)&&!ObjectUtils.isEmpty(alipayItems)&&!ObjectUtils.isEmpty(balancePayItems))
            throw new BusinessException("暂不支持混合支付",ResetApiResult.STATUS_ERROR,1003);
        if(wechatPayItems.size()>1||alipayItems.size()>1||balancePayItems.size()>1)
            throw new BusinessException("暂不支持混合支付",ResetApiResult.STATUS_ERROR,1003);
        String payRequestUuid="";
        if(!ObjectUtils.isEmpty(wechatPayItems)){
            PaymentItem paymentItem=wechatPayItems.get(0);
            if(paymentItem.getUsefulAmount()==null||paymentItem.getUsefulAmount()<=0d)
                throw  new BusinessException("支付金额异常",ResetApiResult.STATUS_ERROR,1003);
            prePayReturnBo.setTradeAmout(new Double(paymentItem.getUsefulAmount()*100).intValue());
            prePayReturnBo.setNeedYiPay(true);
            prePayReturnBo.setPayType("0");
            prePayReturnBo.setPayItemId(paymentItem.getId());
            payRequestUuid=paymentItem.getUuid();
        }else if(!ObjectUtils.isEmpty(alipayItems)){
            PaymentItem paymentItem=alipayItems.get(0);
            if(paymentItem.getUsefulAmount()==null||paymentItem.getUsefulAmount()<=0d)
                throw  new BusinessException("支付金额异常",ResetApiResult.STATUS_ERROR,1003);
            prePayReturnBo.setTradeAmout(new Double(paymentItem.getUsefulAmount()*100).intValue());
            prePayReturnBo.setNeedYiPay(true);
            prePayReturnBo.setPayType("1");
            payRequestUuid=paymentItem.getUuid();
            prePayReturnBo.setPayItemId(paymentItem.getId());
        }else if(!ObjectUtils.isEmpty(balancePayItems)){
            PaymentItem paymentItem=balancePayItems.get(0);
            if (paymentItem.getUsefulAmount() == null || paymentItem.getUsefulAmount() <= 0d)
                throw new BusinessException("支付金额异常", ResetApiResult.STATUS_ERROR, 1003);
            prePayReturnBo.setPayItemId(paymentItem.getId());
            prePayReturnBo.setBanlancePayAmount(paymentItem.getUsefulAmount());
        }
        //如果是POS扫码支付，则需要获取authcode
        if("ScanPay".equalsIgnoreCase(payRequestType)){
            for(PaymentItemBo paymentItemBo:accountingBo.getContent().getPayment().getPaymentItems()){
                if(payRequestUuid.equalsIgnoreCase(paymentItemBo.getUuid()))
                    prePayReturnBo.setAuthCode(paymentItemBo.getAuthCode());
            }
            if(prePayReturnBo.isNeedYiPay()&&ObjectUtils.isEmpty(prePayReturnBo.getAuthCode()))
                throw  new BusinessException("用户授权码为空",ResetApiResult.STATUS_ERROR,1003);
        }else if("MicroPay".equalsIgnoreCase(payRequestType)){
            for(PaymentItemBo paymentItemBo:accountingBo.getContent().getPayment().getPaymentItems()){
                if(payRequestUuid.equalsIgnoreCase(paymentItemBo.getUuid())) {
                    prePayReturnBo.setWechatAppid(paymentItemBo.getWechatAppid());
                    prePayReturnBo.setWechatOpenId(paymentItemBo.getWechatOpenId());
                }
            }
            if(prePayReturnBo.isNeedYiPay()&&(ObjectUtils.isEmpty(prePayReturnBo.getWechatOpenId())||ObjectUtils.isEmpty(prePayReturnBo.getWechatAppid())))
                throw  new BusinessException("Appid或Open id为空",ResetApiResult.STATUS_ERROR,1003);
        }
        return prePayReturnBo;
    }

    @Override
    public StorePaymentParamBo getStorePaymentParamBo(long storeId){
        StorePaymentParamBo storePaymentParamBo=new StorePaymentParamBo();
        storePaymentParamBo.setAppid("hf19102035480OVA");
        storePaymentParamBo.setAppsecret("nfePmU6VhhCPmEZzuyySvFc0xhEEUWiA");
//        storePaymentParamBo.setAppid("hf163356826045OA");
//        storePaymentParamBo.setAppsecret("MgAtKIRKPMMbbfycOw5b87U6NP024kWA");
        String callbackContextPath="http://118.113.200.6:7090/MeiYe";
        storePaymentParamBo.setContextPath(callbackContextPath);
        return storePaymentParamBo;
    }





}
