package com.meiye.bo.part;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class DishSetmealGroupBo extends ParentEntity {
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
}
