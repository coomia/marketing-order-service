package com.meiye.bo.accounting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Created by Administrator on 2018/10/7 0007.
 */
@Data
@NoArgsConstructor
public class InternalApiResult extends AbstractInternalApiResult {

    public InternalApiResult(String state,String message){
        super(state,message);
    }
}
