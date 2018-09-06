package com.meiye.repository.booking;

import  com.meiye.model.booking.BookingTradeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  booking_trade_item
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Repository
public interface BookingTradeItemRepository extends JpaRepository<BookingTradeItem,Long>{

}

