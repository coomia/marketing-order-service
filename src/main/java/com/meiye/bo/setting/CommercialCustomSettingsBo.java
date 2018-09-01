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
 * table name:  commercial_custom_settings
 * author name: ryne
 * create time: 2018-08-26 17:57:14
 */
@Data
public class CommercialCustomSettingsBo extends ParentBo {

	private Long id;
	@JSONField(serialize=false)
	private Long brandIdentity= WebUtil.getCurrentBrandId();
	@JSONField(serialize=false)
	private Long shopIdentity= WebUtil.getCurrentStoreId();
	private Long type;
	private String settingKey;
	private String settingValue;

}

