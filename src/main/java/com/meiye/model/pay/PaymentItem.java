package com.meiye.model.pay;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  payment_item
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Data
@Entity
public class PaymentItem extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private Long paymentId;
	private String paymentUuid;
	private Long payModeId;
	private String payModeName;
	private Double faceAmount;
	private Double usefulAmount;
	private Double changeAmount;
	private String relateId;
	private Integer payStatus;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer isRefund;
	private String payMemo;
	private Integer refundWay;
	private Integer paySource;
}

