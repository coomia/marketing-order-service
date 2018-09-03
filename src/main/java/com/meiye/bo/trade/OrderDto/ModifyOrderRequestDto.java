package com.meiye.bo.trade.OrderDto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 14:57 2018/9/2
 * @Modified By:
 */
@Data
public class ModifyOrderRequestDto implements Serializable {

    private Long brandID;
    private Long shopID;
    private String opVersionUUID;
    private String userId;
    private String deviceID;
    private String versionCode;
    private String versionName;
    private String appType ;
    private String systemType;
    private String opcode;
    private String reqMarker;
    private TradeRequestDto content;

}
