package com.meiye.repository.trade;

import  com.meiye.model.trade.CustomerCardTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  customer_card_time
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface CustomerCardTimeRepository extends JpaRepository<CustomerCardTime,Long>{

}

