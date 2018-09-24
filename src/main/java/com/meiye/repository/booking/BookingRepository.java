package com.meiye.repository.booking;

import  com.meiye.model.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  booking
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>,JpaSpecificationExecutor<Booking> {

}

