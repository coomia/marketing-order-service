package com.meiye.service.pay.impl;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.pay.*;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.TradeBo;
import com.meiye.bo.trade.TradeItemBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.CommercialPaySetting;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.trade.Trade;
import com.meiye.model.trade.TradeItem;
import com.meiye.repository.pay.CommercialPaySettingRepository;
import com.meiye.repository.pay.PaymentItemExtraRepository;
import com.meiye.repository.pay.PaymentItemRepository;
import com.meiye.repository.pay.PaymentRepository;
import com.meiye.repository.trade.TradeItemRepository;
import com.meiye.repository.trade.TradeRepository;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.system.util.WebUtil;
import com.meiye.util.MeiYeInternalApi;
import com.meiye.util.ObjectUtil;
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

    @Autowired
    CommercialPaySettingRepository commercialPaySettingRepository;

    @Override
    public String getAppSercet(Integer storeId){
        return "test";
    }

    @Override
    @Transactional
    public void payFailed(String outTradeNo){
        PaymentItem paymentItem=paymentItemRepository.findOneByUuid(outTradeNo);
        if(paymentItem==null)
            throw new BusinessException("支付记录不存在");
        if(paymentItem.getPayStatus()!=null&&!paymentItem.getPayStatus().equals(1)&&!paymentItem.getPayStatus().equals(2))
            return;

        Timestamp now=new Timestamp(Calendar.getInstance().getTimeInMillis());
        paymentItem.setPayStatus(9);
        paymentItem.setServerUpdateTime(now);
        paymentItemRepository.save(paymentItem);

        Optional<Payment> paymentOptional = paymentRepository.findById(paymentItem.getPaymentId());
        if (!paymentOptional.isPresent())
            throw new BusinessException("支付记录不存在");
        Payment payment = paymentOptional.get();
        payment.setIsPaid(2);
        payment.setServerUpdateTime(now);
        paymentRepository.save(payment);


        Optional<Trade> tradeOption=tradeRepository.findById(payment.getRelateId());
        if(!tradeOption.isPresent())
            throw new BusinessException("销货单不存在.");

        Trade trade=tradeOption.get();
        if(!trade.getTradeType().equals(1))
            throw new BusinessException("销货单不存在.");
        if(trade.getTradePayStatus().equals(1)||trade.getTradePayStatus().equals(2)) {
            trade.setTradePayStatus(9);
            trade.setTradeStatus(4);//支付失败订单状态该是什么？
        }
        trade.setServerUpdateTime(now);
        tradeRepository.save(trade);
    }


    @Override
    @Transactional
    public void paySuccess(String outTradeNo, String yiPayTradeNo){
        PaymentItem paymentItem=paymentItemRepository.findOneByUuid(outTradeNo);
        if(paymentItem==null)
            throw new BusinessException("支付记录不存在");
        if(paymentItem.getPayStatus()!=null&&!paymentItem.getPayStatus().equals(1)&&!paymentItem.getPayStatus().equals(2))
            return;

        Timestamp now=new Timestamp(Calendar.getInstance().getTimeInMillis());
        paymentItem.setPayStatus(3);
        paymentItem.setServerUpdateTime(now);
        paymentItem.setStatusFlag(1);
        paymentItemRepository.save(paymentItem);

        Optional<Payment> paymentOptional = paymentRepository.findById(paymentItem.getPaymentId());
        if (!paymentOptional.isPresent())
            throw new BusinessException("支付记录不存在");
        Payment payment = paymentOptional.get();
        payment.setIsPaid(1);
        payment.setStatusFlag(1);
        payment.setServerUpdateTime(now);
        paymentRepository.save(payment);


        Optional<Trade> tradeOption=tradeRepository.findById(payment.getRelateId());
        if(!tradeOption.isPresent())
            throw new BusinessException("销货单不存在.");

        Trade trade=tradeOption.get();
        if(!trade.getTradeType().equals(1))
            throw new BusinessException("销货单不存在.");

        //调用过翼支付，并且获取到了翼支付的tradeId
        if (!ObjectUtils.isEmpty(yiPayTradeNo)) {
            List<PaymentItemExtra> paymentItemExtras=paymentItemExtraRepository.findByPaymentItemIdAndStatusFlag(paymentItem.getId(),1);
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
                paymentItemExtra.setPaymentItemId(paymentItem.getId());
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
        if(!trade.getTradePayStatus().equals(3)) {
            trade.setTradePayStatus(3);
            trade.setTradeStatus(4);
        }
        trade.setServerUpdateTime(now);
        tradeRepository.save(trade);
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
        MeiYeInternalApi.subtractCommission(trade.getRelateTradeId(),trade.getBrandIdenty(),trade.getShopIdenty());
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
    public PrePayBo savePaymentData(AccountingBo accountingBo, String payRequestType){
        try {
            PrePayBo prePayBo =null;
            Optional<Trade> tradeOption=tradeRepository.findById(accountingBo.getContent().getTrade().getId());
            if(!tradeOption.isPresent())
                throw new BusinessException("销货单不存在。", ResetApiResult.STATUS_ERROR,1001);
            Trade trade=tradeOption.get();
            if(!trade.getServerUpdateTime().equals(accountingBo.getContent().getTrade().getServerUpdateTime()))
                throw new BusinessException("销货单已经被其他人处理。", ResetApiResult.STATUS_ERROR,ResetApiResult.POS_TRADE_CHANGED);
            if(!trade.getTradeType().equals(1))
                throw new BusinessException("销货单不存在。", ResetApiResult.STATUS_ERROR,1001);

            PaymentBo paymentBo=accountingBo.getContent().getPayment();

            Payment payment=paymentRepository.findOneByUuid(paymentBo.getUuid());
            //如果payment不存在，则继续保存payment
            if(payment==null) {
                paymentBo.setRelateId(trade.getId());
                paymentBo.setRelateUuid(trade.getUuid());
                paymentBo.setBizDate(new Date());
                paymentBo.setIsPaid(2);
                paymentBo.setDeviceIdenty(accountingBo.getDeviceId());
                payment = paymentBo.copyTo(Payment.class);
                paymentRepository.save(payment);
            }
            if(!ObjectUtils.isEmpty(paymentBo.getPaymentItems())){
                if(paymentBo.getPaymentItems().size()>1)
                    throw new BusinessException("暂不支持混合支付",ResetApiResult.STATUS_ERROR,1003);
                PaymentItemBo paymentItemBo=paymentBo.getPaymentItems().get(0);
                PaymentItem paymentItem=paymentItemRepository.findOneByUuid(paymentItemBo.getUuid());
                if(paymentItem==null) {
                    paymentItemBo.setPaymentId(payment.getId());
                    paymentItemBo.setDeviceIdenty(accountingBo.getDeviceId());
                    paymentItemBo.setPaymentUuid(payment.getUuid());
                    paymentItemBo.setPayStatus(1);
                    paymentItem = paymentItemBo.copyTo(PaymentItem.class);
                    paymentItemRepository.save(paymentItem);
                }
                if (!ObjectUtils.isEmpty(paymentItemBo.getPaymentItemExtra())) {
                    for (PaymentItemExtraBo paymentItemExtraBo : paymentItemBo.getPaymentItemExtra()) {
                        PaymentItemExtra paymentItemExtra=paymentItemExtraRepository.findOneByUuid(paymentItemExtraBo.getUuid());
                        if(paymentItemExtra==null) {
                            paymentItemExtraBo.setPaymentItemId(paymentItem.getId());
                            paymentItemExtraBo.setDeviceIdenty(accountingBo.getDeviceId());
                            paymentItemExtra = paymentItemExtraBo.copyTo(PaymentItemExtra.class);
                            paymentItemExtraRepository.save(paymentItemExtra);
                        }
                    }
                }
                prePayBo =prePay(payRequestType,paymentItem,paymentItemBo.getAuthCode(),paymentItemBo.getWechatAppid(),paymentItemBo.getWechatOpenId());
            }else
                throw new BusinessException("销货单没有待支付的项.", ResetApiResult.STATUS_ERROR,1001);

            return prePayBo;
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

    public PrePayBo prePay(String payRequestType, PaymentItem paymentItem, String authCode, String wechatAppId, String wechatOpenId){
        if(paymentItem==null||paymentItem.getId()==null)
            throw new BusinessException("销货单没有需要支付的项", ResetApiResult.STATUS_ERROR,1001);
        if(paymentItem.getUsefulAmount()==null)
            throw  new BusinessException("销货单支付金额异常",ResetApiResult.STATUS_ERROR,1003);
        else if((paymentItem.getPayModeId().equals(1l) || paymentItem.getPayModeId().equals(2l))  && paymentItem.getUsefulAmount()<0d)
            throw  new BusinessException("销货单支付金额异常",ResetApiResult.STATUS_ERROR,1003);
        else if((paymentItem.getPayModeId().equals(4l) || paymentItem.getPayModeId().equals(5l))  && paymentItem.getUsefulAmount()<=0d)
            throw  new BusinessException("销货单支付金额异常",ResetApiResult.STATUS_ERROR,1003);


        PrePayBo prePayBo =new PrePayBo();
        prePayBo.setPayRequestType(payRequestType);
        prePayBo.setPaymentItemId(paymentItem.getId());
        prePayBo.setOutTradeNo(paymentItem.getUuid());
        prePayBo.setAuthCode(authCode);
        prePayBo.setWechatAppid(wechatAppId);
        prePayBo.setWechatOpenId(wechatOpenId);

        if(paymentItem.getPayModeId().equals(4l)){
            prePayBo.setWechatPay();
            prePayBo.setNeedYiPay(true);
        }else if(paymentItem.getPayModeId().equals(5l)){
            prePayBo.setAliPay();
            prePayBo.setNeedYiPay(true);
        }else if(paymentItem.getPayModeId().equals(1l)){
            prePayBo.setBanlancePay();
        }else if(paymentItem.getPayModeId().equals(2l)||paymentItem.getPayModeId().equals(3l)){
            prePayBo.setCashPay();
        }else
            throw new BusinessException("销货单不支持的支付类型.", ResetApiResult.STATUS_ERROR,1001);
        prePayBo.setTradeAmount(paymentItem.getUsefulAmount());

        //如果是POS扫码支付，则需要获取authcode
        if("ScanPay".equalsIgnoreCase(payRequestType)){
            if(prePayBo.isNeedYiPay()&&ObjectUtils.isEmpty(prePayBo.getAuthCode()))
                throw  new BusinessException("用户授权码为空",ResetApiResult.STATUS_ERROR,1003);
        }else if("MicroPay".equalsIgnoreCase(payRequestType)){
            if(prePayBo.isNeedYiPay()&&(ObjectUtils.isEmpty(prePayBo.getWechatOpenId())||ObjectUtils.isEmpty(prePayBo.getWechatAppid())))
                throw  new BusinessException("Appid或Open id为空",ResetApiResult.STATUS_ERROR,1003);
        }
        return prePayBo;
    }

    @Override
    public StorePaymentParamBo getStorePaymentParamBo(long storeId){
        try {
            StorePaymentParamBo storePaymentParamBo = new StorePaymentParamBo();
            CommercialPaySetting commercialPaySetting = commercialPaySettingRepository.findOneByShopIdentyAndTypeAndStatusFlag(storeId, 2, 1);
            if (ObjectUtils.isEmpty(commercialPaySetting) || ObjectUtils.isEmpty(commercialPaySetting.getAppid()) || ObjectUtils.isEmpty(commercialPaySetting.getAppsecret()))
                throw new BusinessException("支付设置错误,请联系管理员检查.");

            storePaymentParamBo.setAppid(commercialPaySetting.getAppid());
            storePaymentParamBo.setAppsecret(commercialPaySetting.getAppsecret());
//        storePaymentParamBo.setAppid("hf163356826045OA");
//        storePaymentParamBo.setAppsecret("MgAtKIRKPMMbbfycOw5b87U6NP024kWA");
            String callbackContextPath = "http://b.zhongmeiyunfu.com/MeiYe";
            storePaymentParamBo.setContextPath(callbackContextPath);
            return storePaymentParamBo;
        }catch(Exception exp){
            logger.info("支付设置错误,请联系管理员检查.",exp);
            throw new BusinessException("支付设置错误,请联系管理员检查.");
        }
    }


    @Override
    public String getStoreWechatAppId(long storeId){
        StorePaymentParamBo storePaymentParamBo=new StorePaymentParamBo();
        CommercialPaySetting commercialPaySetting=commercialPaySettingRepository.findOneByShopIdentyAndTypeAndStatusFlag(storeId,1,1);
        if(ObjectUtils.isEmpty(commercialPaySetting)||ObjectUtils.isEmpty(commercialPaySetting.getAppid()))
            throw new BusinessException("小程序设置错误,请联系管理员检查.");
        return commercialPaySetting.getAppid();
    }



    @Override
    @Transactional(noRollbackFor = BusinessException.class)
    public Trade refundPayment(Long paymentItemId){
        return refundByPaymentItemId(paymentItemId);
    }

    @Override
    public Trade refundByPaymentItemId(Long paymentItemId){
        String errorMessage="";
        Optional<PaymentItem> paymentItemOptional=paymentItemRepository.findById(paymentItemId);
        if(!paymentItemOptional.isPresent())
            throw new BusinessException("支付或退款项不存在.");
        PaymentItem paymentItem=paymentItemOptional.get();
        if(ObjectUtil.notEqual(paymentItem.getPayStatus(), 3)&&ObjectUtil.notEqual(paymentItem.getPayStatus(),6))
            throw new BusinessException("退款状态不正确，不允许退款.");

        Optional<Payment> paymentOptional=paymentRepository.findById(paymentItem.getPaymentId());
        if(!paymentOptional.isPresent())
            throw new BusinessException("支付记录不存在.");
        Payment payment=paymentOptional.get();

        Optional<Trade> tradeOptional=tradeRepository.findById(payment.getRelateId());
        if(!tradeOptional.isPresent())
            throw new BusinessException("销货单/退货单不存在.");
        Trade trade=tradeOptional.get();
        //如果是退款失败，再次退款的时候，需要更新退款单号
        if(ObjectUtil.equals(paymentItem.getPayStatus(),6))
            paymentItem.setReturnCode(UUIDUtil.randomUUID());
        if(ObjectUtils.isEmpty(paymentItem.getReturnCode()))
            paymentItem.setReturnCode(UUIDUtil.randomUUID());
        paymentItem.setPayStatus(4); //退款中
        paymentItem.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));


        if (ObjectUtil.equals(paymentItem.getPayModeId(),1l)) {
            InternalApiResult internalApiResult=null;
            if(ObjectUtil.equals(trade.getTradeType(),1))  //销货单
                internalApiResult = MeiYeInternalApi.refund(orderService.getCustomerIdByType(trade.getId(), 3), trade.getId(), paymentItem.getId(), paymentItem.getUsefulAmount(), WebUtil.getCurrentStoreId(), WebUtil.getCurrentBrandId(), paymentItem.getCreatorId(), paymentItem.getCreatorName());
            else //退货单
                internalApiResult = MeiYeInternalApi.refund(orderService.getCustomerIdByType(trade.getRelateTradeId(), 3), trade.getRelateTradeId(), paymentItem.getId(), paymentItem.getUsefulAmount(), WebUtil.getCurrentStoreId(), WebUtil.getCurrentBrandId(), paymentItem.getCreatorId(), paymentItem.getCreatorName());
            if(internalApiResult.isSuccess()) {
                paymentItem.setPayStatus(5);
            } else {
                paymentItem.setPayStatus(6);
                errorMessage="余额支付退还失败.";
            }
        }else if(ObjectUtil.equals(paymentItem.getPayModeId(),2l)||paymentItem.getPayModeId().equals(3l)){
            paymentItem.setPayStatus(5);
        }else if(ObjectUtil.equals(paymentItem.getPayModeId(),4l)||ObjectUtil.equals(paymentItem.getPayModeId(),5l)){
            Integer returnAmt = new Double(paymentItem.getUsefulAmount() * 100).intValue();
            YiPayRefundResponseBo yiPayRefundResponseBo =null;
            PaymentItemExtra paymentItemExtra=paymentItemExtraRepository.findOneByPaymentItemId(paymentItem.getId());
            if(ObjectUtil.equals(trade.getTradeType(),1))  //销货单
                yiPayRefundResponseBo=YiPayApi.refund(getStorePaymentParamBo(WebUtil.getCurrentStoreId()), returnAmt, paymentItem.getUuid(), paymentItemExtra.getPayTranNo(), paymentItem.getReturnCode());
            else //退货单
                yiPayRefundResponseBo=YiPayApi.refund(getStorePaymentParamBo(WebUtil.getCurrentStoreId()), returnAmt, null, paymentItemExtra.getPayTranNo(), paymentItem.getReturnCode());

            if (yiPayRefundResponseBo == null || !yiPayRefundResponseBo.isSuccess()) {
                errorMessage += "翼支付退款失败";
                paymentItem.setPayStatus(6);
            }else {
                paymentItemExtra.setStatusFlag(1);
                paymentItemExtra.setRefundCallbackTime(new Timestamp(System.currentTimeMillis()));
                paymentItemExtra.setRefundTradeNo(yiPayRefundResponseBo.getHaipay_refund_id());
                paymentItemExtra.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
                paymentItemExtraRepository.save(paymentItemExtra);
            }
        }
        paymentItemRepository.save(paymentItem);
        if(!ObjectUtils.isEmpty(errorMessage))
            throw new BusinessException(errorMessage);
        return trade;
    }


    @Override
    @Transactional
    public void updateRefundStatus(String refundNo, boolean refundSucess){
        PaymentItem paymentItem=paymentItemRepository.findOneByReturnCode(refundNo);
        if(ObjectUtil.equals(paymentItem.getPayStatus(),4)){
            paymentItem.setPayStatus(refundSucess?5:6);
        }

        paymentItemRepository.save(paymentItem);
    }

    @Override
    @Transactional
    public void updateRefundStatus(Long paymentItemId, boolean refundSucess){
        Optional<PaymentItem> paymentItemOptional=paymentItemRepository.findById(paymentItemId);
        if(!paymentItemOptional.isPresent())
            throw new BusinessException("退款单号未找到");
        PaymentItem paymentItem=paymentItemOptional.get();
        if(ObjectUtil.equals(paymentItem.getPayStatus(),4)||ObjectUtil.equals(paymentItem.getPayStatus(),3)){
            paymentItem.setPayStatus(refundSucess?5:6);
        }

        paymentItemRepository.save(paymentItem);
    }

    @Override
    public List<PaymentItemBo> getAllPaymentItemListByTradeId(Long tradeId, Long paymentItemId) {
        List<PaymentItem> paymentItems = null;
        if (ObjectUtils.isEmpty(paymentItemId)) {
            Optional<Trade> tradeOptional = tradeRepository.findById(tradeId);
            if (!tradeOptional.isPresent())
                throw new BusinessException("售货单/退货单不存在.");
            Trade trade = tradeOptional.get();
            List<Payment> payments = paymentRepository.findByRelateId(trade.getId());
            if (!ObjectUtils.isEmpty(payments)) {
                List<Long> paymentIds = payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
                paymentItems = paymentItemRepository.findByPaymentIdIn(paymentIds);
            }
        } else {
            List<Long> ids=new ArrayList<>();
            ids.add(paymentItemId);
            paymentItems = paymentItemRepository.findAllById(ids);
        }
        if (!ObjectUtils.isEmpty(paymentItems)) {
            List<PaymentItemBo> paymentItemBos = new ArrayList<>();
            for (PaymentItem paymentItem : paymentItems) {
                PaymentItemBo paymentItemBo = paymentItem.copyTo(PaymentItemBo.class);
                PaymentItemExtra paymentItemExtra = paymentItemExtraRepository.findOneByPaymentItemId(paymentItem.getId());
                List<PaymentItemExtraBo> paymentItemExtraBos = new ArrayList<>();
                paymentItemExtraBos.add(paymentItemExtra.copyTo(PaymentItemExtraBo.class));
                paymentItemBo.setPaymentItemExtra(paymentItemExtraBos);
                paymentItemBos.add(paymentItemBo);
            }
            return paymentItemBos;
        }
        return null;
    }


    @Override
    @Transactional
    public void updateRefundTradeStatus(Long tradeId){
        Optional<Trade> tradeOptional=tradeRepository.findById(tradeId);
        if(!tradeOptional.isPresent())
            throw new BusinessException("退货单不存在.");
        Trade trade=tradeOptional.get();
        if(!ObjectUtil.equals(trade.getTradeType(),2))
            throw new BusinessException("退货单不存在.");
        List<Payment> payments=paymentRepository.findByRelateId(trade.getId());
        boolean refundFailed=false;
        boolean refundSucess=true;
        if(!ObjectUtils.isEmpty(payments)){
            List<Long> paymentIds = payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
            List<PaymentItem> paymentItems=paymentItemRepository.findByPaymentIdIn(paymentIds);
            for(PaymentItem paymentItem:paymentItems){
                if(ObjectUtil.equals(paymentItem.getPayStatus(),6)){
                    refundFailed=true;
                    refundSucess=false;
                    break;
                }else if (ObjectUtil.notEqual(paymentItem.getPayStatus(),5)){
                    refundSucess=false;
                }
            }
        }
        if(refundFailed)
            trade.setTradePayStatus(6);
        else if(refundSucess){
            trade.setTradePayStatus(5);
            trade.setTradeStatus(5);
        }else if(!refundSucess){
            trade.setTradePayStatus(4);
        }
        trade.setServerUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        tradeRepository.save(trade);
    }


}
