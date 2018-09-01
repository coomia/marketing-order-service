package com.meiye.model.setting;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
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
@Entity
public class CommercialCustomSettings extends BusinessParentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	private Long brandIdentity;
//	private Long shopIdentity;
	private Long type;
	private String settingKey;
	private String settingValue;

}

