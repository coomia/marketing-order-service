package com.meiye.model.setting;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
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
@Entity
public class TableArea extends BusinessParentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String areaName;
//	private Long brandIdentity;
//	private Long shopIdentity;
	private String areaCode;
	private String memo;
}

