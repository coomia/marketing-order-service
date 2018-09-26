package com.meiye.bo.accounting;

import com.meiye.bo.pay.PaymentBo;
import com.meiye.bo.trade.TradeBo;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/23 0023.
 */
@Data
public class AccountingContentBo {
    TradeBo trade;
    PaymentBo payment;
}
