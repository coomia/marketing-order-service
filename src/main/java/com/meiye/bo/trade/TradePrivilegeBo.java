package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  trade_privilege
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class TradePrivilegeBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long tradeId;
	private Long tradeItemId;
	private String tradeUuid;
	private String tradeItemUuid;
	private Integer privilegeType;
	private Double privilegeValue;
	private Double privilegeAmount;
	private Long promoId;
	private String surchargeName;
	private String privilegeName;
//	private Long brandIdenty;
//	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private Long couponId;
}

