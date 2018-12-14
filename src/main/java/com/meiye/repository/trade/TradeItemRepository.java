package com.meiye.repository.trade;

import com.meiye.bo.salary.TradeAndUserBo;
import com.meiye.bo.salary.UserAndTradeItm;
import  com.meiye.model.trade.TradeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * table name:  trade_item
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Repository
public interface TradeItemRepository extends JpaRepository<TradeItem,Long>{

    List<TradeItem> findAllByTradeIdAndStatusFlag(Long tradeId,Integer statusFlag);

    List<TradeItem> findAllByTradeId(Long tradeId);

    @Modifying
    @Query(value = "select new com.meiye.bo.salary.UserAndTradeItm (tu.userId ,tm.dishName,tm.dishId ,tm.quantity) "+
            "  from TradeUser tu " +
            "  left join TradeItem tm on tu.tradeItemId = tm.id " +
            "  left join Trade tt on tt.id = tm.tradeId " +
            "  where tu.statusFlag =1 and   tt.statusFlag= 1 and   tt.tradeTime >= ?1 and tt.tradeTime<= ?2  and tt.tradeTime >= ?1 and tt.tradeTime<= ?2 and tu.shopIdenty=?3 and tu.brandIdenty=?4")
    List<UserAndTradeItm> getUserItem(Date start, Date end, Long shopIdenty, Long brandIdenty);

    @Modifying
    @Query(value = "select new com.meiye.bo.salary.UserAndTradeItm (tu.userId ,tm.dishName,tm.dishId ,tm.quantity)" +
            " from TradeUser tu " +
            " left join TradeItem tm on tu.tradeItemId = tm.id " +
            " left join Trade tt on tt.id = tm.tradeId " +
            //" left join Trade tt on tt.id = tu.tradeId and tu.statusFlag =1 " +
            //" left join TradeItem tm on tt.id = tm.tradeId and  tt.statusFlag= 1 and  tt.tradeTime >= ?1 and tt.tradeTime<= ?2" +
            " where tu.statusFlag =1 and   tt.statusFlag= 1 and   tt.tradeTime >= ?1 and tt.tradeTime<= ?2  and tt.tradeTime >= ?1 and tt.tradeTime<= ?2 and tu.shopIdenty=?3 and tu.brandIdenty=?4 and tu.userId = ?5  ")
    List<UserAndTradeItm> getOneUserItem(Date start, Date end, Long shopIdenty, Long brandIdenty,Long userID);
}

