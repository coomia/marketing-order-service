package com.meiye.repository.trade;

import  com.meiye.model.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * table name:  trade
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradeRepository extends JpaRepository<Trade,Long>{

    @Modifying
    @Query(value = "update Trade t set t.tradeStatus = 6 , t.serverUpdateTime = ?2  where t.id = ?1")
    void deleteTradeById(Long id, Timestamp serverUpdateTime);

    Trade findByIdAndBrandIdentyAndTradeStatusIsNot(Long id,Long brandIdenty,Integer tradeStatus);
}

