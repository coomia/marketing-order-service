package com.meiye.bo.accounting;

import lombok.Data;

/**
 * Created by Administrator on 2018/10/6 0006.
 */
@Data
public class MicroPayRequestBo {
    private String appType;
    private Long brandId;
    private String deviceId;
    private Long shopId;
    private String systemType;
    private String versionCode;
    private String versionName;
    private MicroPayRequestContentBo content;
}
