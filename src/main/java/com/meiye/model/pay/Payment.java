package com.meiye.model.pay;

import java.sql.Timestamp;
import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  accounting
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Data
@Entity
public class Payment extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private Date bizDate;
	private Date paymentTime;
	private Integer paymentType;
	private Long relateId;
	private String relateUuid;
	private Double receivableAmount;
	private Double exemptAmount;
	private Double actualAmount;
	private String handoverUuid;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer isPaid;
	private String memo;
	private Integer recycleStatus;
	private Double shopActualAmount;
}

