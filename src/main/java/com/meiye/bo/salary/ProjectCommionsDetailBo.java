package com.meiye.bo.salary;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProjectCommionsDetailBo implements Serializable {

    private Long userId;
    private Long dishId;
    private String dishName;
    private Integer countAll;
    private BigDecimal commissions;
    private BigDecimal total;

    public ProjectCommionsDetailBo(Long userId, Long dishId, String dishName, Integer countAll, BigDecimal commissions, BigDecimal total) {
        this.userId = userId;
        this.dishId = dishId;
        this.dishName = dishName;
        this.countAll = countAll;
        this.commissions = commissions;
        this.total = total;
    }
}
