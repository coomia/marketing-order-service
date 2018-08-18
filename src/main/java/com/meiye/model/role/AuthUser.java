package com.meiye.model.role;

import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long roleId;
	private String name;
	private byte gender;
	private Timestamp birthday;
	private String identityCard;
	private String  education;
	private String graduateSchool;
	private String intoWorkDate;
	private String isMarry;
	private String mobile;
	private String email;
	private long QQ;
	private String address;
	private String icon;
	private String ecName;
	private String ecRelation;
	private String ecMobile;
	private String ecMobileReserve;
	private String jobNumber;
	private String jobEmployeeType;
	private String jobEntryTime;
	private String jobPositiveTime;
	private String jobPosition;
	private String jobGrade;
	private String jobAddress;
	private String salaryCalcMode;
	private String salaryBase;
	private String salaryPost;
	private String account;
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
