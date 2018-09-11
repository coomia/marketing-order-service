package com.meiye.bo.booking;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  booking_trade_item
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
public class BookingTradeItemBo extends BusinessParentBo implements Serializable{

	private Long id;
	private Long bookingId;
	private String bookingUuid;
	private String dishId;
	private String dishName;
	private Integer type;
	private Integer sort;
	private Double actualAmount;
	private String memo;
	private String unitName;
	private String uuid;
	private Date clientCreateTime;
	private Date clientUpdateTime;
}

