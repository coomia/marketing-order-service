package com.meiye.service.config;

import com.meiye.bo.config.AppConfigBo;
import org.springframework.cache.annotation.Cacheable;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Administrator on 2018/8/5 0005.
 */

public interface AppConfigService {
    @Cacheable
    List<AppConfigBo> listAppConfigs();

    AppConfigBo getAppConfig(String appName, String configName);

    AppConfigBo getAppConfig(Long configId);

    void saveConfig(@NotNull AppConfigBo appConfigBo);
}
