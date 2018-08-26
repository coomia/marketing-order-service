package com.meiye.bo.store;

import com.meiye.bo.ParentBo;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
@Data
public class CommercialBo extends ParentBo {
    private Long commercialId;//bigint(20) NOT NULL主键id
    private String commercialName;//varchar(100) NULL
    private String commercialContact;//varchar(50) NULL联系人
    private String commercialPhone;//varchar(50) NULL联系电话
    private String commercialAdress;//varchar(300) NULL
    private String commercialDesc;//varchar(1000) NULL商户描述
    private String commercialLogo;//varchar(300) NULLLOGO对应的URL
    private Integer status;//tinyint(4) NOT NULL商户状态 0-可用 -1-不可用,1-预装
    private Integer invalidStatus;//tinyint(2) NULL作废状态:1 正常 2 已作废
    private Long brandId;//bigint(20) NULL所属品牌id
    private BrandBo brandBo;//品牌
    private String branchName;//varchar(50) NULL分店名称
    private String openTime;//varchar(64) NOT NULL营业时间
    private String consumePerson;//varchar(50) NULL人均消费
    private Integer deviceType;//tinyint(1) NOT NULL商家设备类型，0:pad 1:phone
    private String longitude;//varchar(10) NULL经度
    private String latitude;//varchar(10) NULL纬度
}
