package com.meiye.repository.pay;

import  com.meiye.model.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table name:  accounting
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{


    @Modifying
    @Query(value = "update Payment p set p.statusFlag = 2 where p.relateId = ?1")
    public void disableExistPaymentByRelateId(Long relateId);

    public List<Payment> findByRelateIdAndStatusFlag(Long relateId,Integer statusFlag );
    public List<Payment> findByRelateId(Long relateId);

}

