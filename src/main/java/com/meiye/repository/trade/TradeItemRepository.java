package com.meiye.repository.trade;

import  com.meiye.model.trade.TradeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table name:  trade_item
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradeItemRepository extends JpaRepository<TradeItem,Long>{

    List<TradeItem> findAllByTradeIdAndStatusFlag(Long tradeId,Integer statusFlag);

}

