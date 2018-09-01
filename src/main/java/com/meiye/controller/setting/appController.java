package com.meiye.controller.setting;

import com.meiye.bo.setting.CommercialCustomSettingsBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.service.setting.CommercialCustomerService;
import com.meiye.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:24 2018/9/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/public/api/setting/app",produces="application/json;charset=UTF-8")
public class appController {

    @Autowired
    CommercialCustomerService commercialCustomerService;


    @PostMapping("/saveAppSetting")
    public ResetApiResult saveTableArea(@RequestBody String value){
        if(Objects.isNull(value)){
            throw  new BusinessException("小程序设置失败：未检测到上传数据");
        }
        CommercialCustomSettingsBo commercialCustomSettingsBo = new CommercialCustomSettingsBo();
        commercialCustomSettingsBo.setSettingValue(value);
        commercialCustomSettingsBo.setSettingKey(Constants.APP_KEY);
        commercialCustomSettingsBo.setType(Constants.STORE_SETTING_TYPE);
        commercialCustomerService.deleteCommercialCustomSettings(commercialCustomSettingsBo);
        commercialCustomerService.saveCommercialCustomSettings(commercialCustomSettingsBo);
        return ResetApiResult.sucess("");
    }


}
