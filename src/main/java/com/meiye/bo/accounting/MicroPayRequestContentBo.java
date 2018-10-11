package com.meiye.bo.accounting;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/10/6 0006.
 */
@Data
public class MicroPayRequestContentBo {
    private Long payment_id;
    private Long payment_item_id;
    private Integer total_amount;
    private String out_trade_no;
    private String sub_appid;
    private String sub_openid;
    private String spbill_create_ip;
}
