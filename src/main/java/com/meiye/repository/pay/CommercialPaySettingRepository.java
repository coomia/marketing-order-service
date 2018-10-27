package com.meiye.repository.pay;

import  com.meiye.model.pay.CommercialPaySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  commercial_pay_setting
 * author name: ryne
 * create time: 2018-10-27 18:15:58
 */ 
@Repository
public interface CommercialPaySettingRepository extends JpaRepository<CommercialPaySetting,Long>{
    public CommercialPaySetting findOneByShopIdentyAndTypeAndStatusFlag(Long shopIdenty,Integer type,Integer statusFlag);
}

