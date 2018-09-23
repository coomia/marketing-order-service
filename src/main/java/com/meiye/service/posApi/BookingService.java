package com.meiye.service.posApi;

import com.meiye.bo.booking.dto.BookToOrderResponseDto;
import com.meiye.bo.booking.dto.BookingRequestDto;
import com.meiye.bo.booking.dto.BookingResponseDto;
import com.meiye.bo.booking.dto.CacelBookingDto;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.model.booking.Booking;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 11:19 2018/9/10
 * @Modified By:
 */
public interface BookingService {
    /**
     * 创建预定接口
     * @param bookingRequestDto
     * @return
     */
    BookingResponseDto addBooking(BookingRequestDto bookingRequestDto);

    /**
     * 更新预定接口
     * @param bookingRequestDto
     * @return
     */
    BookingResponseDto updateBooking(BookingRequestDto bookingRequestDto);

    /**
     * 删除预定单
     * @param cacelBookingDto
     * @return
     */
    Booking delBooking(CacelBookingDto cacelBookingDto);

    /**
     * 预定转订单
     * @param addOrderRequestDto
     * @return
     */
    BookToOrderResponseDto updateBookingToOrder(OrderRequestDto addOrderRequestDto);

}
