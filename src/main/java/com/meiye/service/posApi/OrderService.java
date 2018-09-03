package com.meiye.service.posApi;

import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:12 2018/9/1
 * @Modified By:
 */
public interface OrderService {

    void testLog();

    OrderResponseDto modifyOrderData(OrderRequestDto orderRequestDto);

    OrderResponseDto addOrderData(OrderRequestDto addOrderRequestDto);

    OrderResponseDto getOrderResponse(Long tradeId);

}
