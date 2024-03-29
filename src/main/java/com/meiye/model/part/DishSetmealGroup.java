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
public class DishSetmealGroup extends BusinessParentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long setmealDishId;
  private String name;
  private String aliasName;
  private Double orderMin;
  private Double orderMax;
//  private Long brandIdenty;
  private Long sort;
}
