package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  trade_item
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class TradeItemBo extends ParentBo implements Serializable{

	private Long id;
	private Long tradeId;
	private String tradeUuid;
	private Long parentId;
	private String parentUuid;
	private String dishId;
	private Long dishSetmealGroupId;
	private String dishName;
	private Integer type;
	private Integer sort;
	private Double price;
	private Double quantity;
	private Double amount;
	private Double propertyAmount;
	private Double actualAmount;
	private String tradeMemo;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Long tradeTableId;
	private String tradeTableUuid;
	private String batchNo;
	private Integer enableWholePrivilege;
	private String unitName;
	private Long relateTradeItemId;
	private String relateTradeItemUuid;
	private Double feedsAmount;
	private Integer invalidType;
	private Integer recycleStatus;
	private Double returnQuantity;
	private Date clientCreateTime;
	private Date clientUpdateTime;
}

