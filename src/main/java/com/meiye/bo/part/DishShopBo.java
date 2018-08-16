package com.meiye.bo.part;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
public class DishShopBo extends ParentBo {
  @Id
  @GeneratedValue
  private Long id;
  private String uuid;
  @JSONField(serialize=false)
  private Long brandDishId;
  @JSONField(serialize=false)
  private String brandDishUuid;
  private Long dishTypeId;
  private String dishCode;
  private Long type;
  private String name;
  @JSONField(serialize=false)
  private String aliasName;
  @JSONField(serialize=false)
  private String shortName;
  @JSONField(serialize=false)
  private String aliasShortName;
  private String dishNameIndex;
  private String barcode;
  @JSONField(serialize=false)
  private Long unitId;
  private String unitName;
  @JSONField(serialize=false)
  private Double weight;
  private Double marketPrice;
  private Long sort;
  private String dishDesc;
  @JSONField(serialize=false)
  private String videoUrl;
  @JSONField(serialize=false)
  private Long wmType;
  @JSONField(serialize=false)
  private Long saleType;
  @JSONField(serialize=false)
  private Double dishIncreaseUnit;
  private Long isSingle;
  private Long isDiscountAll;
  private Long source;
  @JSONField(serialize=false)
  private Long isSendOutside;
  private Long isOrder;
  @JSONField(serialize=false)
  private Long defProperty;

  private Double stepNum;
  private Long minNum;
  private Long maxUum;
  private Long clearStatus;
  private Long isManual;
  private Double saleTotal;
  @JSONField(serialize=false)
  private Double residueTotal;
  @JSONField(serialize=false)
  private Double saleTotalWechat;
  @JSONField(serialize=false)
  private Double residueTotalWechat;
  @JSONField(serialize=false)
  private java.util.Date validTime;
  @JSONField(serialize=false)
  private java.util.Date unvalidTime;
  @JSONField(serialize=false)
  private String scene;
  private Long shopIdenty;
  @JSONField(serialize=false)
  private Long brandIdenty;
  private Long enabledFlag;
  private String skuKey;
  private Long hasStandard;
  private Double dishQty;
  @JSONField(serialize=false)
  private Long boxQty;
  private Long currRemainTotal;
  @JSONField(serialize=false)
  private Long isChangePrice;
  private Long productId;
  private List<DishSetmealGroupBo> dishSetmealGroupBos;
  private List<DishPropertyBo> dishPropertyBos;
}
