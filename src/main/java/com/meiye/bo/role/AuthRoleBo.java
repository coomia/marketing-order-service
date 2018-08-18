package com.meiye.bo.role;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	private int sort;
	private long brandIdenty;
	private byte sourceFlag;
	private byte enableFlag;
	private byte isCreateAccountByDealer;
	private byte isCreateAccountByShop;

}
