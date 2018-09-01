package com.meiye.service.setting.impl;

import com.meiye.bo.setting.CommercialCustomSettingsBo;
import com.meiye.model.setting.CommercialCustomSettings;
import com.meiye.repository.setting.CommercialCustomSettingRepository;
import com.meiye.service.setting.CommercialCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:33 2018/9/1
 * @Modified By:
 */
@Service
public class CommercialCustomerServiceImpl implements CommercialCustomerService {

    @Autowired
    CommercialCustomSettingRepository  commercialCustomSettingRepository;

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void saveCommercialCustomSettings(CommercialCustomSettingsBo commercialCustomSettingsBo) {
        CommercialCustomSettings commercialCustomSettings = commercialCustomSettingsBo.copyTo(CommercialCustomSettings.class);
        commercialCustomSettings.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        //插入新配置
        commercialCustomSettingRepository.save(commercialCustomSettings);
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void deleteCommercialCustomSettings(CommercialCustomSettingsBo commercialCustomSettingsBo) {
        //根据表的唯一约束判断配置是否存在，存在即删除
        CommercialCustomSettings oldSettingObj = commercialCustomSettingRepository.findByBrandIdentyAndShopIdentyAndSettingKey(commercialCustomSettingsBo.getBrandIdenty(),commercialCustomSettingsBo.getShopIdenty(),commercialCustomSettingsBo.getSettingKey());
        if(Objects.nonNull(oldSettingObj)){
            commercialCustomSettingRepository.deleteById(oldSettingObj.getId());
        }
    }
}
