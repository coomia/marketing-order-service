package com.meiye.service.posApi;

import com.meiye.bo.booking.dto.BookingRequestDto;
import com.meiye.bo.booking.dto.BookingResponseDto;

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

}
