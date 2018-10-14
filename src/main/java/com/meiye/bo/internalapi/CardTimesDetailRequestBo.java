package com.meiye.bo.internalapi;

import lombok.Data;

/**
 * Created by jonyh on 2018/10/14.
 */
@Data
public class CardTimesDetailRequestBo {
    private String dishId;
    private String dishName;
    private Double tradeCount;
    private Long creatorId;
    private String creatorName;
}
