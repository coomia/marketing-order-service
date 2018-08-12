package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class DishSetmealBo extends ParentBo {
  @Id
  @GeneratedValue
  private Long id;
  private Long childDishId;
  private Long dishId;
  private DishShopBo dishShopBo;
  private Long comboDishTypeId;
  private Long childDishType;
  private Double price;
  private Double leastCellNum;
  private Long isReplace;
  private Long isDefault;
  private Long isMulti;
  private Long brandIdenty;
  private Long sort;
}
