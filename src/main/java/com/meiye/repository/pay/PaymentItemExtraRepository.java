package com.meiye.repository.pay;

import com.meiye.model.pay.PaymentItemExtra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table name:  payment_item_extra
 * author name: ryne
 * create time: 2018-09-23 23:57:01
 */ 
@Repository
public interface PaymentItemExtraRepository extends JpaRepository<PaymentItemExtra,Long>{
    public List<PaymentItemExtra> findByPaymentItemIdIn(List<Long> paymentItemId);
    public List<PaymentItemExtra> findByPaymentItemIdInAndStatusFlag(List<Long> paymentItemId, Integer statusFlag);
    public List<PaymentItemExtra> findByPaymentItemIdAndStatusFlag(Long paymentItemId, Integer statusFlag);
    public List<PaymentItemExtra> findByPaymentItemId(Long paymentItemId);
    public PaymentItemExtra findOneByUuid(String uuid);
    public PaymentItemExtra findOneByPaymentItemId(Long paymentItemId);
}

