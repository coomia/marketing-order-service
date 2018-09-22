package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Data
public class QueryYiPayRefundStatusResponseDetailBo {
    private String trade_refund_id;//翼商户系统退款订单号
    private String out_refund_no;//美业系统退款订单号
    private String refund_time;
    private Integer refund_amount;//
    private String refund_status;//0--失败；1--成功；2--处理中；3--转入代发
}
