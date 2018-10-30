package com.meiye.bo.trade;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  trade_user
 * author name: ryne
 * create time: 2018-08-28 22:57:15
 */ 
@Data
public class TradeUserBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long tradeId;
	private String tradeUuid;
	private Long userId;
	private String userName;
	private Integer type;
	private Integer roleId;
	private String roleName;
	private Long tradeItemId;
	private String tradeItemUuid;
//	private Long shopIdenty;
//	private Long brandIdenty;
}

