package com.meiye.service.posApi;

import com.meiye.bo.trade.OrderDto.AddOrderRequestDto;
import com.meiye.bo.trade.OrderDto.ModifyOrderRequestDto;
import com.meiye.bo.trade.OrderDto.ModifyOrderResponseDto;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:12 2018/9/1
 * @Modified By:
 */
public interface OrderService {

    void testLog();

    ModifyOrderResponseDto modifyOrderData(ModifyOrderRequestDto modifyOrderRequestDto);

    ModifyOrderResponseDto addOrderData(AddOrderRequestDto addOrderRequestDto);

}
