package com.meiye.bo.pay;

import lombok.Data;
import org.springframework.util.ObjectUtils;

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

    // -1 失败; 0 成功; 1退款中
    public int refundStatus(){
        if(ObjectUtils.isEmpty(refund_lists))
            return -1;
        for(QueryYiPayRefundStatusResponseDetailBo payRefundStatusResponseDetailBo:refund_lists){
            if(payRefundStatusResponseDetailBo.refundFailed())
                return -1;
            else if(payRefundStatusResponseDetailBo.refundProcessing())
                return 1;
        }
        return 0;
    }
}
