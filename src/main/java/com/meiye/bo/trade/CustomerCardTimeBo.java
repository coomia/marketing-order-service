package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  customer_card_time
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class CustomerCardTimeBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long customerId;
	private Integer recordType;
	private Long tradeId;
	private String tradeUuid;
	private Long dishId;
	private Integer groupId;
	private String dishName;
	private String tradeCount;
	private String residueCount;
//	private Long shopIdenty;
//	private Long brandIdenty;
	private Integer enabledFlag;
}

