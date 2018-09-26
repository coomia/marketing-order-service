package com.meiye.bo.accounting;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/23 0023.
 */
@Data
public class AccountingBo {
    Long brandId;
    Long shopId;
    String deviceId;
    AccountingContentBo content;
    Long operateId;
    String operateName;
}
