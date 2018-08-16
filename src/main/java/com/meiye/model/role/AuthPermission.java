package com.meiye.model.role;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * table name:  auth_permission
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
@Entity
public class AuthPermission extends ParentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long parentId;
	private String levelId;
	private String name;
	private String code;
	private byte type;
	private String url;
	private int sort;
	private byte platform;
	private byte supportVersion;
	private byte sourceFlag;
	private byte groupFlag;
	private byte checked;
	private String zoneCode;

}

