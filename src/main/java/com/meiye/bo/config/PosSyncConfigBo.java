package com.meiye.bo.config;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.ParentBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import java.io.Serializable;
/**
 * table name:  pos_sync_config
 * author name: ryne
 * create time: 2018-09-02 01:49:11
 */ 
@Data
public class PosSyncConfigBo extends ParentBo implements Serializable{

	private Integer id;
	private String syncTable;
	private String requestTable;
	private Integer syncRecentDays;
	private String filterShopIdenty;
	private String filterBrandIdenty;
}

