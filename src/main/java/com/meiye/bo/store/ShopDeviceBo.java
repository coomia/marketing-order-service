package com.meiye.bo.store;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  shop_device
 * author name: ryne
 * create time: 2018-09-05 21:24:59
 */ 
@Data
public class ShopDeviceBo extends ParentBo implements Serializable{

	private Long id;
	private String deviceMac;
	private String deviceNo;
	private Integer deviceType;
	private Long brandIdenty;
	private Long shopIdenty;
}

