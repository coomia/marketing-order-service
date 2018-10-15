package com.meiye.model.part;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class DishShop extends BusinessParentEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String uuid;
  private Long brandDishId;
  private String brandDishUuid;
  private Long dishTypeId;
  private String dishCode;
  private Long type;
  private String name;
  private String aliasName;
  private String shortName;
  private String aliasShortName;
  private String dishNameIndex;
  private String barcode;
  private Long unitId;
  private String unitName;
  private Double weight;
  private Double marketPrice;
  private Long sort;
  private String dishDesc;
  private String videoUrl;
  private Long wmType;
  private Long saleType;
  private Double dishIncreaseUnit;
  private Long isSingle;
  private Long isDiscountAll;
  private Long source;
  private Long isSendOutside;
  private Long isOrder;
  private Long defProperty;
  private Double stepNum=1d;
  private Long minNum=1l;
  private Long maxUum=1l;
  private Long clearStatus;
  private Long isManual;
  private Double saleTotal;
  private Double residueTotal;
  private Double saleTotalWechat;
  private Double residueTotalWechat;
  private java.util.Date validTime;
  private java.util.Date unvalidTime;
  private String scene;
//  private Long shopIdenty;
//  private Long brandIdenty;
  private Long enabledFlag;
  private String skuKey;
  private Long hasStandard;
  private Double dishQty=1d;
  private Long boxQty;
  private Long currRemainTotal;
  private Long isChangePrice;
  private Long productId;
}
