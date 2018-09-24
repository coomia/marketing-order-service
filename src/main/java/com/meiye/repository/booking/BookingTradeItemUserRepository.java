package com.meiye.repository.booking;

import  com.meiye.model.booking.Booking;
import  com.meiye.model.booking.BookingTradeItemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * table name:  booking_trade_item_user
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Repository
public interface BookingTradeItemUserRepository extends JpaRepository<BookingTradeItemUser,Long>{

    @Query(value = "select u.userId from Booking b, BookingTradeItemUser u where b.id = u.bookingId " +
            " and u.shopIdenty =?1 and  u.brandIdenty = ?2 and b.startTime <= ?3 and b.endTime >= ?4 and b.deviceIdenty = ?5")
    Set<Long> getAllAlreadyBookedUser(Long shopID , Long brandID, Date startTime, Date endTime ,String deviceID);

    List<BookingTradeItemUser> findByBookingIdAndStatusFlag(Long bookingId,Integer stuatsFlage);

}

