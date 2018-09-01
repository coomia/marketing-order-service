package com.meiye.model.trade;

import java.util.Date;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  wx_trade_customer
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
@Entity
public class WxTradeCustomer extends BusinessParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private Long customerId;
	private Integer status;
	private Long tradeId;
	private String tradeUuid;
	private Long dishId;
	private String dishName;
//	private Long shopIdenty;
//	private Long brandIdenty;
	private Integer enabledFlag;
}

