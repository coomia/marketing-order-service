package com.meiye.model.pay;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  commercial_pay_setting
 * author name: ryne
 * create time: 2018-10-27 18:15:58
 */ 
@Data
@Entity
public class CommercialPaySetting extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private String appsecret;
	private String appid;
	private Integer type;
	private Long brandIdenty;
	private Long shopIdenty;
}

