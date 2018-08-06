package com.meiye.bo.config;

import com.meiye.model.config.AppConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
public class AppConfigBo {
    private Long id;
    private String appName;
    private String configName;
    private String configValue;
    private String status;
    private Long updateId;
    private Date updateDatetime;
    private Long entryId;
    private Date entryDatetime;
    private Long version;

    public static AppConfigBo copy(AppConfig appConfig){
        if(appConfig!=null) {
            AppConfigBo appConfigBo = new AppConfigBo();
            BeanUtils.copyProperties(appConfig, appConfigBo);
            return appConfigBo;
        }else
            return null;
    }
}
