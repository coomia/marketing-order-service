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
 * table name:  trade_item_property
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
@Entity
public class TradeItemProperty extends BusinessParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private Long tradeId;
	private String tradeUuid;
	private Long tradeItemId;
	private String tradeItemUuid;
	private Integer propertyType;
	private String propertyUuid;
	private String propertyName;
	private Double price;
	private Double quantity;
	private Double amount;
//	private Long brandIdenty;
//	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer recycleStatus;
}

