package com.meiye.model.config;

import com.meiye.system.*;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
@Entity
@Table(name = "app_config")
public class AppConfig extends ParentEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String appName;
    private String configName;
    private String configValue;
    private String status;
    @Version
    private Long version;
}
