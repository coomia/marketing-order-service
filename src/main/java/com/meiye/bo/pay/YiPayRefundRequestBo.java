package com.meiye.bo.pay;

import com.meiye.util.YiPayApi;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Data
public class YiPayRefundRequestBo {
    private String appid;
    private String sign;
    private Integer refund_fee;
    private String out_trade_no;
    private String trade_id;
    private String out_refund_no;
    private String nonce_str;
    private String version;
    private String openid;
}
