package com.meiye.service.pay;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.pay.PaymentItemBo;
import com.meiye.bo.pay.PrePayBo;
import com.meiye.bo.pay.StorePaymentParamBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.trade.Trade;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public interface PayService {
    String getAppSercet(Integer storeId);

    @Transactional
    void payFailed(String outTradeNo);

    @Transactional
    Long paySuccess(String outTradeNo, String yiPayTradeNo);

    void refundSuccessful(Long tradeId);

    void refundFailed(Long tradeId);

    void afterPaySucess(Long tradeId);

    @Transactional
    PrePayBo savePaymentData(AccountingBo accountingBo, String payRequestType);

    List<Payment> findPaymentsByTradeId(Long relateId, boolean includeInactive);

    List<PaymentItem> findPaymentItemsByPamentId(List<Long> paymentIds, boolean includeInactive);

    List<PaymentItemExtra> findPaymentItemExtraByPamentItemId(List<Long> paymentItemIds, boolean includeInactive);

    StorePaymentParamBo getStorePaymentParamBo(long storeId);

    String getStoreWechatAppId(long storeId);

    @Transactional(noRollbackFor = BusinessException.class)
    Trade refundPayment(Long paymentItemId);

    Trade refundByPaymentItemId(Long paymentItemId);

    @Transactional
    void updateRefundStatus(String refundNo, boolean refundSucess);

    @Transactional
    void updateRefundStatus(Long paymentItemId, boolean refundSucess);

    List<PaymentItemBo> getAllPaymentItemListByTradeId(Long tradeId, Long paymentItemId);

    void updateRefundTradeStatus(Long tradeId);
}
