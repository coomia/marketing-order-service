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
 * table name:  trade
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
@Entity
public class Trade extends BusinessParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	private Date bizDate ;
	private Integer domainType;
	private Integer businessType;
	private Integer tradeType;
	private String serialNumber;
	private Date tradeTime;
	private Integer tradeStatus;
	private Integer tradePayStatus;
	private Integer deliveryType;
	private Integer source;
	private String tradeNo;
	private Integer dishKindCount;
	private Double saleAmount;
	private Double privilegeAmount;
	private Double tradeAmount;
	private Double tradeAmountBefore;
	private String tradeMemo;
	private Long relateTradeId;
	private String relateTradeUuid;
//	private Long brandIdenty;
//	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer tradePeopleCount;
}

