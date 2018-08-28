package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class DishSetmealBo extends ParentBo {
  private Long id;
  private Long childDishId;
  private Long dishId;
  private DishShopBo dishShopBo;
  private Long comboDishTypeId;
  private Long childDishType=0l;
  private Double price;
  private Double leastCellNum;
  private Long isReplace;
  private Long isDefault;
  private Long isMulti;
  private Long brandIdenty= WebUtil.getCurrentBrandId();
  private Long sort;
}
