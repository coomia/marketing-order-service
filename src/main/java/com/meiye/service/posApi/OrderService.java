package com.meiye.service.posApi;

import com.meiye.bo.trade.CancelTrade.CancelTradeBo;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.bo.trade.TradeBo;
import com.meiye.model.trade.Trade;

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

    OrderResponseDto getOrderResponse(Long tradeId,boolean needDelData);

    OrderResponseDto deleteTrade(CancelTradeBo cancelTradeBo);

    OrderResponseDto returnTrade(CancelTradeBo cancelTradeBo);

    Trade getTradeByTradeNo(String tradeNo);

    TradeBo getTradeByTradeId(Long tradeId);
}
