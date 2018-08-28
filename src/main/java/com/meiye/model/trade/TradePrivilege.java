package com.meiye.model.trade;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  trade_privilege
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
@Entity
public class TradePrivilege extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private Long tradeId;
	private Long tradeItemId;
	private String tradeUuid;
	private String tradeItemUuid;
	private Integer privilegeType;
	private Double privilegeValue;
	private Double privilegeAmount;
	private Long promoId;
	private String surchargeName;
	private String privilegeName;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
}

