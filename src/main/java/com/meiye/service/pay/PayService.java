package com.meiye.service.pay;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.pay.PrePayReturnBo;
import com.meiye.bo.pay.StorePaymentParamBo;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public interface PayService {
    String getAppSercet(Integer storeId);

    @Transactional
    void yipaySuccess(String tradeNo, String yiPayTradeNo, Long paymentId);

    @Transactional
    void paySuccess(Long tradeId, Long paymentId, String yiPayTradeNo);

    String returnPayment(Long tradeId);

    void refundSuccessful(Long tradeId);

    void refundFailed(Long tradeId);

    void afterPaySucess(Long tradeId);

    void savePaymentData(AccountingBo accountingBo);

    List<Payment> findPaymentsByTradeId(Long relateId, boolean includeInactive);

    List<PaymentItem> findPaymentItemsByPamentId(List<Long> paymentIds, boolean includeInactive);

    List<PaymentItemExtra> findPaymentItemExtraByPamentItemId(List<Long> paymentItemIds, boolean includeInactive);

    @Transactional
    PrePayReturnBo prePay(AccountingBo accountingBo, String payRequestType);

    StorePaymentParamBo getStorePaymentParamBo(long storeId);

    String getStoreWechatAppId(long storeId);
}
