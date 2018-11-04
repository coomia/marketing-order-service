package com.meiye.bo.salary;

import lombok.Data;

import java.io.Serializable;

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

    public UserAndTradeItm(Long userId, String dishName, String dishId) {
        this.userId = userId;
        this.dishName = dishName;
        this.dishId = dishId;
    }
}
