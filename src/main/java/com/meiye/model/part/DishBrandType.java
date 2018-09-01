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
public class DishBrandType extends BusinessParentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long parentId;
  private String typeCode;
  private String name;
  private String aliasName;
  private Long sort;
  private String dishTypeDesc;
  private Long isOrder;
  private String uuid;
//  private Long brandIdenty;
  private Long enabledFlag;
  private Long isCure;
}
