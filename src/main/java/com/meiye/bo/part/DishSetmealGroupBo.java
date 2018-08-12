package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
public class DishSetmealGroupBo extends ParentBo {
  @Id
  @GeneratedValue
  private Long id;
  private Long setmealDishId;
  private String name;
  private String aliasName;
  private Double orderMin;
  private Double orderMax;
  private Long brandIdenty;
  private Long sort;
  private List<DishSetmealBo> dishSetmealBos;
}
