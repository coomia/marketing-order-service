package com.meiye.model.part;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class DishUnitDictionary extends ParentEntity {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String aliasName;
  private Long brandIdenty;
}
