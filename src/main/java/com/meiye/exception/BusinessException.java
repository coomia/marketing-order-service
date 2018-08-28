package com.meiye.exception;

import com.meiye.bo.system.ResetApiResult;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/28 0028.
 */
@Data
public class BusinessException extends RuntimeException {
    private Integer statusCode= ResetApiResult.STATUS_CODE_200;
    private String messageType=ResetApiResult.STATUS_ERROR;

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String message,String messageType){
        super(message);
        this.messageType=messageType;
    }

    public BusinessException(String message,String messageType,Integer statusCode){
        super(message);
        this.messageType=messageType;
        this.statusCode=statusCode;
    }
}
