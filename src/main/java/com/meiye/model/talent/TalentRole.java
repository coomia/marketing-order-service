package com.meiye.model.talent;

import com.meiye.model.BusinessParentEntity;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class TalentRole extends BusinessParentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //'自增主键ID',
    private Long roleId;
    private Long planId;
//    private Long shopIdenty;//'如果归属门店，则为门店id；如果归属品牌，则为       品牌id.\r\n新的权限体系下，全部为品牌id\r\n就是登录标示!!仅登录使用',
//    private Long brandIdenty; // '品牌标识 : 品牌标识',
}
