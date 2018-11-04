package com.meiye.bo.pay;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@Data
public class QueryTradePaymentContentBo {
    Long paymentItemId;
    Long tradeId;
}
