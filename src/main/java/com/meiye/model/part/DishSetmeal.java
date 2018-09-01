package com.meiye.model.part;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class DishSetmeal extends BusinessParentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
//  private Long brandIdenty;
  private Long sort;
}
