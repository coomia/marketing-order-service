package com.meiye.bo.pay;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  payment_item_extra
 * author name: ryne
 * create time: 2018-09-23 23:57:01
 */ 
@Data
public class PaymentItemExtraBo extends BusinessParentBo implements Serializable{

	private Long id;
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
//	private Long brandIdenty;
//	private Long shopIdenty;
	private String deviceIdenty;
}

