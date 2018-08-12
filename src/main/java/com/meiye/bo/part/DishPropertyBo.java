package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class DishPropertyBo extends ParentBo {
  @Id
  @GeneratedValue
  private Long id;
  private Long propertyTypeId;
  private Long propertyKind;
  private String name;
  private String aliasName;
  private Double reprice;
  private Long sort;
  private Long brandIdenty;
  private String uuid;
  private Long isCure;
  private Long enabledFlag;
}
