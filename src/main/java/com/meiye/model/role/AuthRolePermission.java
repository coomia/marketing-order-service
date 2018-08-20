package com.meiye.model.role;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * table name:  auth_role_permission
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
@Entity
public class AuthRolePermission  extends ParentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long roleId;
	private long permissionId;
	private long brandIdenty;
	private byte platform;
	private Integer groupFlag;

}

