package com.meiye.bo.part;

import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class DishUnitDictionaryBo extends BusinessParentBo {
  private Long id;
  private String name;
  private String aliasName;
//  private Long brandIdenty;
}
