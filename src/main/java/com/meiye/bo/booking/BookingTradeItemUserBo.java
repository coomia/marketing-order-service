package com.meiye.bo.booking;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  booking_trade_item_user
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
public class BookingTradeItemUserBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long bookingId;
	private Long bookingTradeItemId;
	private String bookingTradeItemUuid;
	private Long userId;
	private String userName;
	private Integer roleId;
	private Integer roleName;
	private Integer isAssign;
}

