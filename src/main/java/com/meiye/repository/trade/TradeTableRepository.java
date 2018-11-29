package com.meiye.repository.trade;

import com.meiye.model.trade.TradeCustomer;
import  com.meiye.model.trade.TradeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table name:  trade_table
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradeTableRepository extends JpaRepository<TradeTable,Long>{

    List<TradeTable> findAllByTradeIdAndStatusFlag(Long tradeId, Integer statusFlag);

    List<TradeTable> findAllByTradeId(Long tradeId);

    @Modifying
    @Query("update TradeTable tt set tt.selfTableStatus=1,serverUpdateTime=now() where tt.tradeId=?1")
    int updateTradeTableSelfTableStatus(Long tradeId);
}

