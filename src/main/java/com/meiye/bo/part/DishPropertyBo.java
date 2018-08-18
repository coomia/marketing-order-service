package com.meiye.bo.part;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
public class DishPropertyBo extends ParentBo {
  @Id
  @GeneratedValue
  private Long id;
  private Long propertyTypeId ;
  private Long propertyKind = 1L;
  private String name;
  private String aliasName;
  private Double reprice;
  private Long sort =1L;
  private Long brandIdenty = WebUtil.getCurrentStoreId();
  private String uuid = UUID.randomUUID().toString();
  private Long isCure;
  private Long enabledFlag ;
  private Long dishShopId;
}
