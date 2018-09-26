package com.meiye.bo.accounting;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@Data
public class WriteOffResultBo {
    private String state;
    private String msg;
    private Object data;

    public boolean isSuccess(){
        return "1000".equals(state);
    }
}
