package com.meiye.repository.pay;

import  com.meiye.model.pay.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * table name:  payment_item
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItem,Long>{

    public List<PaymentItem> findByPaymentIdIn(List<Long>  paymentId);
    public List<PaymentItem> findByPaymentIdInAndStatusFlag(List<Long> paymentId, Integer statusFlag);
    public List<PaymentItem> findByPaymentIdAndStatusFlag(Long paymentId,Integer statusFlag);

}

