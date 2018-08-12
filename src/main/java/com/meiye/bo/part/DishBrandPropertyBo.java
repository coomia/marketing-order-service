package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class DishBrandPropertyBo extends ParentBo {
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
