package com.meiye.model.booking;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  booking_trade_item
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
@Entity
public class BookingTradeItem extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	private Long bookingId;
	private String bookingUuid;
	private String dishId;
	private String dishName;
	private Integer type;
	private Integer sort;
	private Double actualAmount;
	private String memo;
	private Long brandIdenty;
	private Long shopIdenty;
	private String unitName;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
}

