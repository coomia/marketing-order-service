package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  trade
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class TradeBo extends ParentBo implements Serializable{

	private Long id;
	private Date bizDate;
	private Integer domainType;
	private Integer businessType;
	private Integer tradeType;
	private String serialNumber;
	private Date tradeTime;
	private Integer tradeStatus;
	private Integer tradePayStatus;
	private Integer deliveryType;
	private Integer source;
	private String tradeNo;
	private Integer dishKindCount;
	private Double saleAmount;
	private Double privilegeAmount;
	private Double tradeAmount;
	private Double tradeAmountBefore;
	private String tradeMemo;
	private Long relateTradeId;
	private String relateTradeUuid;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Integer tradePeopleCount;
}
