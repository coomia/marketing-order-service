package com.meiye.repository.trade;

import com.meiye.bo.salary.ProjectCommionsDetailBo;
import com.meiye.bo.salary.TradeAndUserBo;
import  com.meiye.model.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

    Trade findByTradeNo(String tradeNo);

    List<Trade> findByRelateTradeIdAndTradeType(Long relateTradeId,Integer tradeType);

    @Modifying
    @Query(value = "select new com.meiye.bo.salary.TradeAndUserBo(" +
            "tu.tradeId,tu.userId,tu.userName,tu.roleId,tu.roleName, " +
            " au.salaryBase,au.salaryPost, " +
            " tt.businessType,tt.tradeType ,tt.tradeStatus ,tt.saleAmount ,tt.tradePayStatus) " +
            " from Trade tt " +
            " inner join TradeUser tu on tt.id = tu.tradeId and tu.statusFlag = 1" +
            " inner join AuthUser au on tu.userId = au.id " +
            " where tt.tradeTime >=?1 and  tt.tradeTime <=?2 and tt.shopIdenty =?3 and tt.brandIdenty = ?4  ")
    List<TradeAndUserBo> getAllSalaryTrade(Date start, Date end, Long shopIdenty, Long brandIdenty);
}

