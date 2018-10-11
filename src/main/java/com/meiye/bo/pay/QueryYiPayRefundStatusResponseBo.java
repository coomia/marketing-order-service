package com.meiye.bo.pay;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Data
public class QueryYiPayRefundStatusResponseBo {
    private String code;
    private String err_msg;
    private List<QueryYiPayRefundStatusResponseDetailBo> refund_lists;

    public boolean isSuccess(){
        return "0".equalsIgnoreCase(code);
    }

}
