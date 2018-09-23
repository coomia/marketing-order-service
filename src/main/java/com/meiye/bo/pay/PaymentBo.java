package com.meiye.bo.pay;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  payment
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Data
public class PaymentBo extends ParentBo implements Serializable{

	private Long id;
	private Date bizDate;
	private Date paymentTime;
	private Integer paymentType;
	private Long relateId;
	private String relateUuid;
	private Double receivableAmount;
	private Double exemptAmount;
	private Double actualAmount;
	private String handoverUuid;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer isPaid;
	private String memo;
	private Integer recycleStatus;
	private Double shopActualAmount;
}

