package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  trade_customer
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class TradeCustomerBo extends ParentBo implements Serializable{

	private Long id;
	private Long tradeId;
	private String tradeUuid;
	private Integer customerType;
	private Long customerId;
	private String customerPhone;
	private String customerName;
	private Integer customerSex;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
}

