package com.meiye.model.config;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
@Entity
@Table(name = "app_config")
public class AppConfig {
    @Id
    @GeneratedValue
    private Long id;
    private String appName;
    private String configName;
    private String configValue;
    private String status;
    private Long updateId;
    private Date updateDatetime;
    private Long entryId;
    private Date entryDatetime;
    @Version
    private Long version;
}
