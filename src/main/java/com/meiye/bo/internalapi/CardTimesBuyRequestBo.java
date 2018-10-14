package com.meiye.bo.internalapi;

import lombok.Data;

import java.util.List;

/**
 * Created by jonyh on 2018/10/14.
 */
@Data
public class CardTimesBuyRequestBo {
    private Long customerId;
    private Long tradeId;
    private String tradeUuid;
    private Long shopId;
    private Long brandId;
    private List<CardTimesDetailRequestBo> dishs;

}
