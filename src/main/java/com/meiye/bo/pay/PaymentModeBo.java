package com.meiye.bo.pay;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  payment_mode
 * author name: ryne
 * create time: 2018-09-22 23:22:36
 */ 
@Data
public class PaymentModeBo extends ParentBo implements Serializable{

	private Long id;
	private String name;
	private Integer paymentModeType;
	private String paymentModeName;
	private Integer sort;
}

