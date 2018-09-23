package com.meiye.repository.pay;

import  com.meiye.model.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  payment
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{

}

