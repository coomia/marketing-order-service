package com.meiye.bo.salary;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProjectCommionsDetailBo implements Serializable {

    private Long userId;
    private String dishId;
    private String dishName;
    private Integer countAll;
    private BigDecimal commissions;


    public ProjectCommionsDetailBo() {
    }
}
