package com.meiye.bo.accounting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Created by Administrator on 2018/10/7 0007.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractInternalApiResult {
    private String state;
    private String msg;
    public boolean isSuccess(){
        return "1000".equals(state);
    }
}
