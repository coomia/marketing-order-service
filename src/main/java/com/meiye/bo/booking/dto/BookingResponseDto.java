package com.meiye.bo.booking.dto;

import com.meiye.bo.booking.BookingTradeItemBo;
import com.meiye.bo.booking.BookingTradeItemUserBo;
import com.meiye.model.booking.Booking;
import com.meiye.model.booking.BookingTradeItem;
import com.meiye.model.booking.BookingTradeItemUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 11:21 2018/9/10
 * @Modified By:
 */
@Data
public class BookingResponseDto implements Serializable {

    private Booking booking;
    private List<BookingTradeItem> bookingTradeItems;
    private List<BookingTradeItemUser> bookingTradeItemUsers;

}
