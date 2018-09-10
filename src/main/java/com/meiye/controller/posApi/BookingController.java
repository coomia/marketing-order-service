package com.meiye.controller.posApi;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.booking.dto.BookingRequestDto;
import com.meiye.bo.booking.dto.BookingResponseDto;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.exception.BusinessException;
import com.meiye.service.posApi.BookingService;
import com.meiye.service.posApi.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

}
