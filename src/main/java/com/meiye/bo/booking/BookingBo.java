package com.meiye.bo.booking;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  booking
 * author name: ryne
 * create time: 2018-09-05 21:26:39
 */ 
@Data
public class BookingBo extends ParentBo implements Serializable{

	private Long id;
	private Long commercialId;
	private Long commercialName;
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
	private Integer confirmed;
	private Long brandIdenty;
	private Long shopIdenty;
	private String deviceIdenty;
	private Date clientCreateTime;
	private Date clientUpdateTime;
	private String  creatorName;
}

