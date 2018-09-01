package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  trade_table
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class TradeTableBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long tradeId;
	private String tradeUuid;
	private Long tableId;
	private String tableName;
//	private Long brandIdenty;
//	private Long shopIdenty;
	private String deviceIdenty;
	private String uuid;
	private Integer tablePeopleCount;
	private String memo;
	private Integer selfTableStatus;
	private Date clientCreateTime;
	private Date clientUpdateTime;
}

