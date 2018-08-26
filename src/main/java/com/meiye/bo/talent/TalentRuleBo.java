package com.meiye.bo.talent;

import com.meiye.bo.ParentBo;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class TalentRuleBo extends ParentBo {
    private Long id;
    private Long ruleType; //提出算法规则 1：固定金额提成  2：按比例提成',
    private String ruleValue;//提成算法基数',
    private String ruleCommission; // '提成数字  如：5% 或固定金额10元',
    private Long planId; //'对于的方案ID',
    private Long dishShopId;//'项目提成时对应的品项ID',
    private Long shopIdenty; //'如果归属门店，则为门店id；如果归属品牌，则为       品牌id.\r\n新的权限体系下，全部为品牌id\r\n就是登录标示!!仅登录使用',
    private Long brandIdenty; //'品牌标识 : 品牌标识',
}
