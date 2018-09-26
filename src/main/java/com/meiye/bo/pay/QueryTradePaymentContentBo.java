package com.meiye.bo.pay;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@Data
public class QueryTradePaymentContentBo {
    String paymentItemId;
    String paymentItemUuid;
    Bool returnPayment;
    Long tradeId;
}
