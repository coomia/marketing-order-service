package com.meiye.repository.trade;

import  com.meiye.model.trade.TradeCustomer;
import com.meiye.model.trade.TradeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table name:  trade_customer
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradeCustomerRepository extends JpaRepository<TradeCustomer,Long>{

    List<TradeCustomer> findAllByTradeIdAndStatusFlag(Long tradeId, Integer statusFlag);

    List<TradeCustomer> findAllByTradeId(Long tradeId);

}

