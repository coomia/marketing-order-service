package com.meiye.model.trade;

import com.meiye.model.BusinessParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * table name:  customer_card_time
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
@Entity
public class CustomerCardTime extends BusinessParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	private Long customerId;
	private Integer recordType;
	private Long tradeId;
	private String tradeUuid;
	private Long dishId;
	private Integer groupId;
	private String dishName;
	private String tradeCount;
	private String residueCount;
//	private Long shopIdenty;
//	private Long brandIdenty;
	private Integer enabledFlag;
}

