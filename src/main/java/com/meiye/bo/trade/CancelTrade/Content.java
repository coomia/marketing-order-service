package com.meiye.bo.trade.CancelTrade;

import lombok.Data;

import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/9/2 22:15
 * @Version 1.0
 */
@Data
public class Content {
    private ObsoleteRequest obsoleteRequest;
    private List<ReturnInventoryItem> returnInventoryItems;
    private boolean reviseStock;
}
