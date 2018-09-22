package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
@Data
public class MicroAppPayResponseBo {
    private String code;
    private String appid;
    private String time_stamp;
    private String nonce_str;
    private String prepay_id;
    private String sign_type;
}
