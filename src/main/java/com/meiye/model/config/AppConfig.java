package com.meiye.model.config;

import com.meiye.bo.config.AppConfigBo;
import com.meiye.model.ParentEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
@Entity
@Table(name = "app_config")
public class AppConfig extends ParentEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String appName;
    private String configName;
    private String configValue;
    private Integer appendInt;
    private String appendString;
    private Date appendDate;
    private Integer statusFlag;
    private Long version;
}
