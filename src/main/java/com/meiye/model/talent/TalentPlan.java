package com.meiye.model.talent;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
public class TalentPlan extends BusinessParentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String planName; //'方案名称'
    private Long planType;  //'方案类型'
    private Long planState;//'方案状态'
    private Long planMode;//'提成方式'
    private Integer enabledFlag;
    private Timestamp enabledTime;
//    private Long shopIdenty;//'如果归属门店，则为门店id；如果归属品牌，则为       品牌id.\r\n新的权限体系下，全部为品牌id\r\n就是登录标示!!仅登录使用',
//    private Long brandIdenty;//'品牌标识 : 品牌标识',
}
