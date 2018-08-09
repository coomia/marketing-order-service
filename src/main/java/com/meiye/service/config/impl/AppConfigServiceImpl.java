package com.meiye.service.config.impl;

import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.user.UserBo;
import com.meiye.model.config.AppConfig;
import com.meiye.repository.config.AppConfigRepository;
import com.meiye.service.config.AppConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Service
@Transactional
public class AppConfigServiceImpl implements AppConfigService {

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Cacheable
    @Override
    public List<AppConfigBo> listAppConfigs(){
        List<AppConfig> appConfigs=appConfigRepository.findAll();
        return copy(appConfigs);
    }

    @Override
    public AppConfigBo getAppConfig(String appName, String configName) {
        List<AppConfigBo> configBos = listAppConfigs();
        AppConfigBo appConfigBo = null;
        if (configBos != null)
            appConfigBo = configBos.stream().filter(configBo -> configBo.getAppName().equalsIgnoreCase(appName) && configBo.getConfigName().equalsIgnoreCase(configName)).findFirst().get();
        return null;
    }

    @Override
    public AppConfigBo getAppConfig(Long configId){
        List<AppConfigBo> configBos = listAppConfigs();
        return configBos.stream().filter(appConfigBo -> appConfigBo.getId().equals(configId)).findFirst().get();
    }

    @Override
    public void saveConfig(@NotNull AppConfigBo appConfigBo){
        AppConfig appConfig=appConfigBo.copyTo(AppConfig.class);
        appConfigRepository.save(appConfig);
    }




    private AppConfigBo copy(AppConfig appconfig){
        return appconfig==null?null:appconfig.copyTo(AppConfigBo.class);
    }

    private List<AppConfigBo> copy(List<AppConfig> appconfigs){
        if(appconfigs!=null&&!appconfigs.isEmpty()) {
            List<AppConfigBo> appConfigBos = new ArrayList<AppConfigBo>();
            appconfigs.stream().filter(appConfig->appConfig!=null).forEach(appConfig->appConfigBos.add(copy(appConfig)));
            return appConfigBos;
        }else
            return null;
    }

}
