package com.meiye.bo.trade.CancelTrade;

import lombok.Data;

/**
 * @Author: Shawns
 * @Date: 2018/9/2 22:14
 * @Version 1.0
 */
@Data
public class ReturnInventoryItem {
    private Long dishId;
    private Long quantity;
    private String dishName;
}
