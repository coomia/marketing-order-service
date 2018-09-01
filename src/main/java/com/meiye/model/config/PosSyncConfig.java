package com.meiye.model.config;

import java.util.Date;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;/**
 * table name:  pos_sync_config
 * author name: ryne
 * create time: 2018-09-02 01:49:11
 */ 
@Data
@Entity
public class PosSyncConfig extends ParentEntity implements Serializable{

	 @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Integer id;
	private String syncTable;
	private String requestTable;
	private Integer syncRecentDays;
	private String filterShopIdenty;
	private String filterBrandIdenty;
}

