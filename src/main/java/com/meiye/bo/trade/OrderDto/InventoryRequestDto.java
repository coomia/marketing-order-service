package com.meiye.bo.trade.OrderDto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:10 2018/9/2
 * @Modified By:
 */
@Data
public class InventoryRequestDto implements Serializable {

    List<InventoryItemsDto> deductInventoryItems;
    List<InventoryItemsDto> returnInventoryItems;

}
