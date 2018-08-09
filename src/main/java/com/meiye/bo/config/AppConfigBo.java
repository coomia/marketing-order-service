package com.meiye.bo.config;

import com.meiye.bo.ParentBo;
import com.meiye.model.config.AppConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
public class AppConfigBo extends ParentBo{
    private Long id;
    private String appName;
    private String configName;
    private String configValue;
    private Long version;


}
