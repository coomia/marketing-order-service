package com.meiye.bo.salary;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Shawns
 * @Date: 2018/10/24 21:53
 * @Version 1.0
 */
@Data
public class TradeAndUserBo implements Serializable{
    private Long tradeId;
    private Long userId;
    private String userName;
    private Integer roleId;
    private String roleName;
    private String salaryBase;
    private String salaryPost;
    private Integer businessType;
    private Integer tradeType;
    private Integer tradeStatus;
    private Double saleAmount;
    private Integer tradePayStatus;

    public TradeAndUserBo(Long tradeId, Long userId, String userName,
                          Integer roleId, String roleName, String salaryBase,
                          String salaryPost, Integer businessType, Integer tradeType,
                          Integer tradeStatus, Double saleAmount,Integer tradePayStatus) {
        this.tradeId = tradeId;
        this.userId = userId;
        this.userName = userName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.salaryBase = salaryBase;
        this.salaryPost = salaryPost;
        this.businessType = businessType;
        this.tradeType = tradeType;
        this.tradeStatus = tradeStatus;
        this.saleAmount = saleAmount;
        this.tradePayStatus = tradePayStatus;
    }
}
