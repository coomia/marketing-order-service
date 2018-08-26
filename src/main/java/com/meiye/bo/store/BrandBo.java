package com.meiye.bo.store;

import com.meiye.bo.ParentBo;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
@Data
public class BrandBo extends ParentBo {
    private Long id;//bigint(20) unsigned NOT NULL主键id
    private String name;//varchar(32) NOT NULL商户组名称
    private String logo;//varchar(128) NOT NULL商户组logo
    private String contacts;//varchar(50) NULL商户组联系人
    private String contactsPhone;//varchar(50) NULL联系人电话
    private String contactsMail;//varchar(50) NULL联系人邮箱
    private String commercialGroupAdress;//varchar(300) NULL商户组地址
    private Integer status;//tinyint(1) NOT NULL状态：0有效 -1无效
    private Long parentId;//bigint(20) NULL父ID=0时，代表的是集团
    private Integer type;//tinyint(1) NULL类型 0代表集团 1代表公司
}
