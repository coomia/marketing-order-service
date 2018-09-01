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
public class DishProperty extends BusinessParentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long propertyTypeId;
  private Long propertyKind;
  private Long dishShopId;
  private String name;
  private String aliasName;
  private Double reprice;
  private Long sort;
//  private Long brandIdenty;
  private String uuid;
  private Long isCure;
  private Long enabledFlag;
}
