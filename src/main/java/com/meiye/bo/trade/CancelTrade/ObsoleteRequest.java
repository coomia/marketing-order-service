package com.meiye.bo.trade.CancelTrade;

import lombok.Data;

/**
 * @Author: Shawns
 * @Date: 2018/9/2 22:13
 * @Version 1.0
 */
@Data
public class ObsoleteRequest {
    private Long tradeId;
    private Long reasonId;
    private String reasonContent;
}
