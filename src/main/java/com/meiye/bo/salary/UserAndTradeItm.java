package com.meiye.bo.salary;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Shawns
 * @Date: 2018/10/28 10:27
 * @Version 1.0
 */
@Data
public class UserAndTradeItm  implements Serializable{
    private Long userId;
    private String dishName;
    private String dishId;
    private Double qty;

    public UserAndTradeItm(Long userId, String dishName, String dishId,Double qty) {
        this.userId = userId;
        this.dishName = dishName;
        this.dishId = dishId;
        this.qty = qty;
    }




}
