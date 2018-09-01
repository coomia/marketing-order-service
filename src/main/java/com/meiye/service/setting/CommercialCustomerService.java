package com.meiye.service.setting;

import com.meiye.bo.setting.CommercialCustomSettingsBo;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:29 2018/9/1
 * @Modified By:
 */
public interface CommercialCustomerService {

    void saveCommercialCustomSettings(CommercialCustomSettingsBo commercialCustomSettingsBo);

    void deleteCommercialCustomSettings(CommercialCustomSettingsBo commercialCustomSettingsBo);
}
