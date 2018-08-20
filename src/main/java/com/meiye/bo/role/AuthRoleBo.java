package com.meiye.bo.role;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import com.meiye.model.role.AuthRolePermission;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * table name:  auth_role
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
public class AuthRoleBo extends ParentBo {
	private long id;
	private String name;
	private String code;
	private int sort =1;
	private long brandIdenty ;
	private byte sourceFlag;
	private byte enableFlag =1;
	private byte isCreateAccountByDealer;
	private byte isCreateAccountByShop;
	//权限集合
	private List<AuthRolePermissionBo> authRolePermissions = new ArrayList<>();

}

