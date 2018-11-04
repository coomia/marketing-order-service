package com.meiye.model.booking;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  booking
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
@Entity
public class Booking extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Long creatorId;
	private String uuid;
	private Long commercialId;
	private String commercialName;
	private Long commercialGender;
	private String commercialPhone;
	private Date startTime;
	private Date endTime;
	private String tableId;
	private String tableName;
	private Integer cousterNum;
	private Integer orderStatus;
	private String remark;
	private Date cancelOrderTime;
	private Integer bookingSource;
	private Integer bookingType;
	private Boolean confirmed;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private String  creatorName;
}

