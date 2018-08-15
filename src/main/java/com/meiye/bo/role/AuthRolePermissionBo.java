package com.meiye.bo.role;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * table name:  auth_role_permission
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
@Entity
public class AuthRolePermissionBo extends ParentBo {
	@Id
	@GeneratedValue
	private long id;
	private long roleId;
	private long permissionId;
	private long brandIdenty;
	private byte platform;
	private byte groupFlag;

}

