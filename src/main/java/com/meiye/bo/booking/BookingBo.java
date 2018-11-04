package com.meiye.bo.booking;

import java.sql.Timestamp;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.ParentBo;
import com.meiye.bo.trade.TradeCustomerBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * table name:  booking
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
public class BookingBo extends BusinessParentBo implements Serializable{

	private Long id;
	private String uuid;
	private Long creatorId;
	private Long commercialId;
	private String commercialName;
	private Long commercialGender;
	private String commercialPhone;
	private Date startTime;
	private Date endTime;
	private String tableId;
	private String tableName;
	private Integer cousterNum;
	private Integer orderStatus;
	private String remark;
	private Date cancelOrderTime;
	private Integer bookingSource;
	private Integer bookingType;
	private Boolean confirmed;
	private String deviceIdenty;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private String  creatorName;
	private List<BookingTradeItemUserBo> bookingTradeItemUsers;
	private List<TradeCustomerBo> tradeCustomerBos;
	private List<BookingTradeItemBo> bookingTradeItems;

}

