package com.meiye.bo.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import com.meiye.system.util.WebUtil;
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
public class AuthRolePermissionBo extends ParentBo {
	private Long id;
	private Long roleId;
	private Long permissionId;
	private AuthPermissionBo authPermissionBo;
	@JSONField(serialize=false)
	private Long brandIdenty=WebUtil.getCurrentBrandId();
	private Integer platform;
	private Integer groupFlag;

}

