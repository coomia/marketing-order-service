package com.meiye.bo.role;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * table name:  auth_permission
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
public class AuthPermissionBo extends ParentBo {
	private Long id;
	private Long parentId;
	private String levelId;
	private String name;
	private String code;
	private Byte type;
	private String url;
	private Integer sort;
	private Byte platform;
	private Byte supportVersion;
	private Byte sourceFlag;
	private Byte groupFlag;
	private Byte checked;
	private String zoneCode;

}

