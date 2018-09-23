package com.meiye.model.pay;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  payment_mode
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Data
@Entity
public class PaymentMode extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private String name;
	private Integer paymentModeType;
	private String paymentModeName;
	private Integer sort;
}

