package com.meiye.model.setting;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * table name:  tables
 * author name: ryne
 * create time: 2018-08-26 17:57:14
 */
@Data
@Entity
public class Tables extends ParentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String tableNum;
	private Long brandIdentity;
	private Long shopIdentity;
	private String tableName;
	private Integer sort;
	private Integer tableStatus;
	private Long areaId;
	private Integer canBooking;
	private String updatorName;
	private Long updatorId;
}

