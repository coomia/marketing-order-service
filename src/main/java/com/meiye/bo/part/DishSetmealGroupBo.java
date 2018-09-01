package com.meiye.bo.part;

import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
public class DishSetmealGroupBo extends BusinessParentBo {
  private Long id;
  private Long setmealDishId=1l;
  private String name;
  private String aliasName;
  private Double orderMin=0d;
  private Double orderMax=0d;
//  private Long brandIdenty= WebUtil.getCurrentBrandId();
  private Long sort=0l;
  private List<DishSetmealBo> dishSetmealBos;
}
