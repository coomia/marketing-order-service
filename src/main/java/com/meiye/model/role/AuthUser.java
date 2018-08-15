package com.meiye.model.role;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * table name:  auth_user
 * author name: ryne
 * create time: 2018-08-15 22:12:36
 */
@Data
@Entity
public class AuthUser extends ParentEntity {
	@Id
	@GeneratedValue
	private long id;
	private long roleId;
	private String account;
	private String name;
	private String mobile;
	private byte mobileStatus;
	private byte gender;
	private Timestamp birthday;
	private String email;
	private long QQ;
	private String address;
	private String icon;
	private String password;
	private String passwordNum;
	private byte sourceFlag;
	private String salt;
	private long shopIdenty;
	private long brandIdenty;
	private byte enabledFlag;
	private byte assignedGroup;
	private long assignedId;

}

