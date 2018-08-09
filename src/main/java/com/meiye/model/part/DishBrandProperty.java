package com.meiye.model.part;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class DishBrandProperty extends ParentEntity {
  @GeneratedValue
  @Id
  private Long id;
  private Long propertyKind;
  private Long propertyId;
  private Long propertyTypeId;
  private Long dishId;
  private String dishName;
  private Long brandIdenty;
  private Long isDefault;
}
