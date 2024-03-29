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
public class DishUnitDictionary extends BusinessParentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String aliasName;
//  private Long brandIdenty;
}
