package com.meiye.model.pay;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  payment_item_extra
 * author name: ryne
 * create time: 2018-09-23 23:57:01
 */ 
@Data
@Entity
public class PaymentItemExtra extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;
	private String uuid;
	private Long paymentItemId;
	private String payTranNo;
	private Integer payType;
	private Integer payChannel;
	private Double handlingCharge;
	private Double handlingChargeRate;
	private String buyerAccount;
	private Date payCallbackTime;
	private String sellerAccount;
	private Integer sellerAccountType;
	private String refundTradeNo;
	private Date refundCallbackTime;
	private Long customerId;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
}

