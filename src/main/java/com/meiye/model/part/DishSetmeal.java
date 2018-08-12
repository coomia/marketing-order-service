package com.meiye.model.part;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class DishSetmeal extends ParentEntity {
  @Id
  @GeneratedValue
  private Long id;
  private Long childDishId;
  private Long dishId;
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