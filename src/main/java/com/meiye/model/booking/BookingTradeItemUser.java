package com.meiye.model.booking;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  booking_trade_item_user
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
@Entity
public class BookingTradeItemUser extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	private Long bookingId;
	private Long bookingTradeItemId;
	private String bookingTradeItemUuid;
	private Long userId;
	private String userName;
	private Integer roleId;
	private String roleName;
	private Long brandIdenty;
	private Long shopIdenty;
	private Integer isAssign;
}

