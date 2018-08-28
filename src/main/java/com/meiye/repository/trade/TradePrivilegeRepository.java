package com.meiye.repository.trade;

import  com.meiye.model.trade.TradePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  trade_privilege
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradePrivilegeRepository extends JpaRepository<TradePrivilege,Long>{

}

