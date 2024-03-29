package com.meiye.repository.trade;

import com.meiye.model.trade.TradeItemProperty;
import  com.meiye.model.trade.TradeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table name:  trade_user
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradeUserRepository extends JpaRepository<TradeUser,Long>{

    List<TradeUser> findAllByTradeIdAndStatusFlag(Long tradeId, Integer statusFlag);

    List<TradeUser> findAllByTradeId(Long tradeId);

}

