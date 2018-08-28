package com.meiye.model.trade;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  trade_user
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
@Entity
public class TradeUser extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private Long tradeId;
	private Long userId;
	private String userName;
	private Integer type;
	private Integer roleId;
	private String roleName;
	private Long tradeItemId;
	private String tradeItemUuid;
	private Long shopIdenty;
	private Long brandIdenty;
}

