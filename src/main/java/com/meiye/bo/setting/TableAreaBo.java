package com.meiye.bo.setting;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * table name:  table_area
 * author name: ryne
 * create time: 2018-08-26 17:57:14
 */
@Data
public class TableAreaBo extends ParentBo {

	private Long id;
	private String areaName;
	@JSONField(serialize=false)
	private Long brandDishId= WebUtil.getCurrentBrandId();
	@JSONField(serialize=false)
	private Long shopIdentity= WebUtil.getCurrentStoreId();
	private String areaCode;
	private String memo;
}

