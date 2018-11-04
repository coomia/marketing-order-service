package com.meiye.model.customer;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  customer
 * author name: ryne
 * create time: 2018-10-30 20:23:12
 * create time: 2018-10-30 23:15:48
 */
@Data
@Entity
public class Customer extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private String uuid;
	private String name;
	private Integer gender;
	private String mobile;
	private Date birthday;
	private Long groupLevelId;
	private String groupLevel;
	private String telephone;
	private String email;
	private String hobby;
	private String address;
	private String profile;
	private Long relateId;
	private String thirdId;
	private String password;
	private Integer integral;
	private Integer sourceId;
	private Long expandedId;
	private Date consumptionLastTime;
	private Double consumptionAmount;
	private Integer consumptionNumber;
	private Integer consumptionIntegral;
	private Double storedBalance;
	private Integer cardResidueCount;
	private Long shopIdenty;
	private Long brandIdenty;
	private Integer enabledFlag;
}

