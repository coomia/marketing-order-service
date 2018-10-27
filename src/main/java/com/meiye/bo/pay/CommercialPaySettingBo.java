package com.meiye.bo.pay;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  commercial_pay_setting
 * author name: ryne
 * create time: 2018-10-27 18:15:58
 */ 
@Data
public class CommercialPaySettingBo extends ParentBo implements Serializable{

	private Long id;
	private String appsecret;
	private String appid;
	private Integer type;
	private Long brandIdenty;
	private Long shopIdenty;
}

