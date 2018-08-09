package com.meiye.bo.part;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class DishUnitDictionaryBo extends ParentEntity {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String aliasName;
  private Long brandIdenty;
}
