package com.meiye.bo.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * table name:  auth_role
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
public class AuthRoleBo extends ParentBo {
	private Long id;
	private String name;
	private String code;
	private Integer sort =1;
	@JSONField(serialize=false)
	private Long brandIdenty=WebUtil.getCurrentBrandId();
	private Integer sourceFlag;
	private Integer enableFlag =1;
	private Integer isCreateAccountByDealer;
	private Integer isCreateAccountByShop;
	//权限集合
	private List<AuthRolePermissionBo> authRolePermissions = new ArrayList<>();

}

