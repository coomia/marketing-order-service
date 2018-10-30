package com.meiye.service.posApi;

import com.meiye.bo.booking.dto.*;
import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.model.booking.Booking;
import com.meiye.model.talent.TalentPlan;
import org.springframework.data.domain.Page;

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

    ReadyBookedUserResponseDto getAllAlreadyBookedUser(ReadyBookedUserRequestDto readyBookedUserRequestDto);

    Page<Booking> getBookPageByCriteria(Integer pageNum, Integer pageSize, BookingPageRequestDto bookingPageRequestDto);


    BookingPageResponseDto getPageBooking(Integer page, Integer pageCount, BookingPageRequestDto bookingPageRequestDto);

    /**
     * 更新微信预订单状态
     * @return
     */
    Booking updateWxBookingStatus(WxBookingRequestDto wxBookingRequestDto);

}

