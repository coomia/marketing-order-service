package com.meiye.bo.part;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Data
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
  @JSONField(serialize=false)
  private Long isOrder =  new Long(1);
  @JSONField(serialize=false)
  private String uuid = UUID.randomUUID().toString();
  @JSONField(serialize=false)
  private Long brandIdenty= WebUtil.getCurrentBrandId();
  private Long enabledFlag;
  @JSONField(serialize=false)
  private Long isCure = new Long(2);
  private List<DishBrandTypeBo> dishBrandTypeBoList;

}
