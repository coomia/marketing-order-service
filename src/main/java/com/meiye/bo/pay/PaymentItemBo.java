package com.meiye.bo.pay;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * table name:  payment_item
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Data
public class PaymentItemBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long paymentId;
	private String paymentUuid;
	private Long payModeId;
	private String payModeName;
	private Double faceAmount;
	private Double usefulAmount;
	private Double changeAmount;
	private String relateId;
	private Integer payStatus;
//	private Long brandIdenty;
//	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer isRefund;
	private String payMemo;
	private Integer refundWay;
	private Integer paySource;
	private List<PaymentItemExtraBo> paymentItemExtra;
	private String authCode;
	private String wechatAppid;
	private String wechatOpenId;
	private String returnCode;
}

