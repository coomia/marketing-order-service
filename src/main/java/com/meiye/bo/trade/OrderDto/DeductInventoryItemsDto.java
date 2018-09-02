package com.meiye.bo.trade.OrderDto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:20 2018/9/2
 * @Modified By:
 */
@Data
public class DeductInventoryItemsDto implements Serializable {

    private Long dishId;
    private Double quantity;
    private String dishName;
    private Double price;

}
