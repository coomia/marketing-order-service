package com.meiye.bo.booking.dto;

import com.meiye.model.booking.Booking;
import com.meiye.model.booking.BookingTradeItem;
import com.meiye.model.booking.BookingTradeItemUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/9/24 20:37
 * @Version 1.0
 */
@Data
public class BookingPageResponseDto implements Serializable {
    private List<Booking> bookings;
    private List<BookingTradeItem> bookingTradeItems;
    private List<BookingTradeItemUser> bookingTradeItemUsers;
}
