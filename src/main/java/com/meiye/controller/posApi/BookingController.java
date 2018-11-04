package com.meiye.controller.posApi;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.booking.dto.*;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.bo.trade.OrderDto.TradeRequestDto;
import com.meiye.exception.BusinessException;
import com.meiye.model.booking.Booking;
import com.meiye.service.posApi.BookingService;
import com.meiye.service.posApi.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:44 2018/9/8
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/pos/api/booking",produces="application/json;charset=UTF-8")
public class BookingController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    BookingService bookingService;

    @PostMapping("/addBookingData")
    public PosApiResult addBookingData(@RequestBody BookingRequestDto bookingRequestDto){
        if(Objects.isNull(bookingRequestDto)){
            logger.error("创建预定接口-预定接口数据为空");
            throw new BusinessException("创建预定接口-预定接口数据为空，请检查！");
        }
        logger.info("创建预定接口-上传json数据："+ JSON.toJSON(bookingRequestDto).toString());
        try {
            BookingResponseDto bookingResponseDto =bookingService.addBooking(bookingRequestDto);
            return PosApiResult.sucess(bookingResponseDto);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("创建预定接口- 预定订单失败！");
        }
    }

    @PostMapping("/updateBookingData")
    public PosApiResult updateBookingData(@RequestBody BookingRequestDto bookingRequestDto){
        if(Objects.isNull(bookingRequestDto)){
            logger.error("修改预定接口-修改预定接口数据为空");
            throw new BusinessException("修改预定接口-修改预定接口数据为空，请检查！");
        }
        logger.info("修改预定接口-上传json数据："+ JSON.toJSON(bookingRequestDto).toString());
        try {
            BookingResponseDto bookingResponseDto =bookingService.updateBooking(bookingRequestDto);
            return PosApiResult.sucess(bookingResponseDto);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("修改预定接口- 修改预定订单失败！");
        }
    }

    @PostMapping("/delBookingData")
    public PosApiResult delBookingData(@RequestBody CacelBookingDto cacelBookingDto){
        if(Objects.isNull(cacelBookingDto)){
            logger.error("删除预定接口-接口数据为空");
            throw new BusinessException("删除预定接口-接口数据为空，请检查！");
        }
        logger.info("删除预定接口-上传json数据："+ JSON.toJSON(cacelBookingDto).toString());
        try {
            Booking booking = bookingService.delBooking(cacelBookingDto);
            return PosApiResult.sucess(booking);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("删除预定接口- 删除预定订单失败！");
        }
    }

    @PostMapping("/updateBookingToOrder")
    public PosApiResult updateBookingToOrder(@RequestBody OrderRequestDto orderRequestDto){
        if(Objects.isNull(orderRequestDto)){
            logger.error("预订转订单接口-接口数据为空");
            throw new BusinessException("预订转订单接口-接口数据为空，请检查！");
        }
        logger.info("预订转订单接口-上传json数据："+ JSON.toJSON(orderRequestDto).toString());
        try {
            BookToOrderResponseDto bookToOrderResponseDto = bookingService.updateBookingToOrder(orderRequestDto);
            return PosApiResult.sucess(bookToOrderResponseDto);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("预订转订单接口- 预订转订单失败！");
        }
    }

    @PostMapping("/getAllReadyBookedUser")
    public PosApiResult getAllAlreadyBookedUser(@RequestBody ReadyBookedUserRequestDto readyBookedUserRequestDto){
        if(Objects.isNull(readyBookedUserRequestDto)){
            logger.error("检查技师列表接口-接口数据为空");
            throw new BusinessException("检查技师列表接口-接口数据为空，请检查！");
        }
        logger.info("检查技师列表接口-上传json数据："+ JSON.toJSON(readyBookedUserRequestDto).toString());
        try {
            ReadyBookedUserResponseDto allAlreadyBookedUser = bookingService.getAllAlreadyBookedUser(readyBookedUserRequestDto);
            return PosApiResult.sucess(allAlreadyBookedUser);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("检查技师列表接口-获取失败");
        }
    }

    @PostMapping("/getPageBooking")
    public PosApiResult getPageBooking(@RequestBody BookingPageRequestDto bookingPageRequestDto){
        if(Objects.isNull(bookingPageRequestDto)){
            logger.error("获取预订单接口列表-接口数据为空");
            throw new BusinessException("获取预订单接口列表-接口数据为空，请检查！");
        }
        if (bookingPageRequestDto.getContent() ==null){
            logger.error("获取预订单接口列表-接口数据验证失败");
            throw new BusinessException("获取预订单接口列表-接口数据验证失败，请检查！");
        }
        logger.info("获取预订单接口列表-上传json数据："+ JSON.toJSON(bookingPageRequestDto).toString());
        try {
            BookingPageResponseDto pageBooking = bookingService.getPageBooking(bookingPageRequestDto.getContent().getPage()
                    , bookingPageRequestDto.getContent().getPageCount(), bookingPageRequestDto);
            return PosApiResult.sucess(pageBooking);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("获取预订单接口列表- 失败！");
        }
    }

    @PostMapping("/updateWxBookingStatus")
    public PosApiResult updateWxBookingStatus(@RequestBody WxBookingRequestDto wxBookingRequestDto){
        if(Objects.isNull(wxBookingRequestDto)){
            logger.error("更新微信预订单接口-接口数据为空");
            throw new BusinessException("更新微信预订单接口-接口数据为空，请检查！");
        }
        logger.info("更新微信预订单接口-上传json数据："+ JSON.toJSON(wxBookingRequestDto).toString());
        try {
            Booking booking = bookingService.updateWxBookingStatus(wxBookingRequestDto);
            return PosApiResult.sucess(booking);
        }catch (BusinessException b){

            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("更新微信预订单接口- 更新微信预订单失败！");
        }
    }


}
