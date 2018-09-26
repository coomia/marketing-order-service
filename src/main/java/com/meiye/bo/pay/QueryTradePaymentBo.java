package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@Data
public class QueryTradePaymentBo {
    String appType;
    Long brandID;
    String deviceID;
    String opVersionUUID;
    Long shopID;
    String systemType;
    String timeZone;
    String versionCode;
    String versionName;
    QueryTradePaymentContentBo content;
}
