package com.meiye.model.store;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  shop_device
 * author name: ryne
 * create time: 2018-09-05 21:24:59
 */ 
@Data
@Entity
public class ShopDevice extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	private String deviceMac;
	private String deviceNo;
	private Integer deviceType;
	private Long brandIdenty;
	private Long shopIdenty;
}

