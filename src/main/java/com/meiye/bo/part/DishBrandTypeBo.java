package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishBrandTypeBo extends ParentBo {
  @Id
  @GeneratedValue
  private Long id;
  private Long parentId;
  private String typeCode;
  private String name;
  private String aliasName;
  private Long sort;
  private String dishTypeDesc;
  private Long isOrder;
  private String uuid;
  private Long brandIdenty;
  private Long enabledFlag;
  private Long isCure;
}
