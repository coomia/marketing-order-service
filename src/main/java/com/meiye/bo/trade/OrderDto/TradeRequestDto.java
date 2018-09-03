package com.meiye.bo.trade.OrderDto;

import com.meiye.bo.trade.TradeBo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:04 2018/9/2
 * @Modified By:
 */
@Data
public class TradeRequestDto implements Serializable {
    private TradeBo tradeRequest;
    private InventoryRequestDto inventoryRequest;
}
