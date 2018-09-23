package com.meiye.bo.pay;

import com.meiye.bo.BusinessParentBo;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/16 0016.
 */
@Data
public class StorePaymentParamBo extends BusinessParentBo {
    private String appid;
    private String appsecret;
    private String contextPath;
}
