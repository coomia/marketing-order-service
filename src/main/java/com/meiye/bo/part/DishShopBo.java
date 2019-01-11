package com.meiye.bo.part;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class DishShopBo extends BusinessParentBo {
  private Long id;
  @JSONField(serialize=false)
  private String uuid = UUID.randomUUID().toString();
  @JSONField(serialize=false)
  private Long brandDishId= WebUtil.getCurrentBrandId();
  @JSONField(serialize=false)
  private String brandDishUuid=UUID.randomUUID().toString();
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
  @JSONField(serialize=false)
  private String barcode;
  @JSONField(serialize=false)
  private Long unitId;
  private String unitName;
  @JSONField(serialize=false)
  private Double weight;
  private Double marketPrice;
  private Long sort=1l;
  @JSONField(serialize=false)
  private String dishDesc;
  @JSONField(serialize=false)
  private String videoUrl;
  @JSONField(serialize=false)
  private Long wmType;
  @JSONField(serialize=false)
  private Long saleType;
  @JSONField(serialize=false)
  private Double dishIncreaseUnit=1d;
  private Long isSingle=1l;
  private Long isDiscountAll=1l;
  private Long source=1l;
  @JSONField(serialize=false)
  private Long isSendOutside=1l;
  private Long isOrder=1l;
  @JSONField(serialize=false)
  private Long defProperty=1l;

  private Double stepNum=1d;
  private Long minNum=1l;
  private Long maxUum=1l;
  private Long clearStatus=1l;
  private Long isManual;
  private Double saleTotal=0d;
  @JSONField(serialize=false)
  private Double residueTotal=0d;
  @JSONField(serialize=false)
  private Double saleTotalWechat=0d;
  @JSONField(serialize=false)
  private Double residueTotalWechat=0d;
  @JSONField(serialize=false)
  private java.util.Date validTime=new Date();
  @JSONField(serialize=false)
  private java.util.Date unvalidTime=new Date();
  @JSONField(serialize=false)
  private String scene="100";
//  private Long shopIdenty=WebUtil.getCurrentStoreId();
//  @JSONField(serialize=false)
//  private Long brandIdenty=WebUtil.getCurrentBrandId();
  private Long enabledFlag;
  private String skuKey;
  private Long hasStandard=2l;
  private Double dishQty=1d;
  @JSONField(serialize=false)
  private Long boxQty=1l;
  private Long currRemainTotal;
  @JSONField(serialize=false)
  private Long isChangePrice;
  private Long productId;
  private List<DishSetmealGroupBo> dishSetmealGroupBos;
  private List<DishPropertyBo> dishPropertyBos;
  Integer pageNum;
  Integer pageSize;

  public Double getDishIncreaseUnit(){
    if(this.dishIncreaseUnit==null||this.dishIncreaseUnit<1d)
      return 1d;
    return dishIncreaseUnit;
  }
}
