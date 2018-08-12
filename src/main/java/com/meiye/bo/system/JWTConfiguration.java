package com.meiye.bo.system;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt.configuration")
public class JWTConfiguration {
    private String secret;
    private Long timeOut;
    private String tokenInHeader;
    private String validTokenStartWith;
}
