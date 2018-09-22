package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Data
public class QueryYiPayRefundStatusRequestBo {
    private String appid;
    private String sign;
    private String out_trade_no;
    private String trade_id;
    private String nonce_str;
    private String version;
}
