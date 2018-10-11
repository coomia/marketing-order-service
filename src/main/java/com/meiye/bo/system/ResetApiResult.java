package com.meiye.bo.system;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
@Data
public class ResetApiResult extends ApiResult {
    private Integer statusCode;
    private String message;
    private String messageType;
    private Object data;
    private String dataType="JSON";

    public static final String STATUS_SUCCESS="info";
    public static final String STATUS_ERROR="error";
    public static final String STATUS_WARNING="warning";
    public static final String STATUS_IGNORE="ignore";

    public static final int STATUS_CODE_200=200;
    public static final int STATUS_CODE_404=404;
    public static final int STATUS_CODE_403=403;
    public static final int STATUS_CODE_500=500;
    private ResetApiResult(){}

    public ResetApiResult(String message,String messageType,Integer statusCode,Object data){
        this.setData(data==null?new Object():data);
        this.setMessage(message);
        this.setMessageType(messageType);
        this.setStatusCode(statusCode==null?200:statusCode);
    }

    public static ResetApiResult sucess(Object data){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data==null?new Object():data);
        resetApiResult.setMessage("");
        resetApiResult.setMessageType(STATUS_IGNORE);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return resetApiResult;
    }

    public static ResetApiResult error(Object data, String errorMessage){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data==null?new Object():data);
        resetApiResult.setMessage(errorMessage);
        resetApiResult.setMessageType(STATUS_ERROR);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return resetApiResult;
    }


    public static ResetApiResult sucessWithWarning(Object data, String warningMessage){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data==null?new Object():data);
        resetApiResult.setMessage(warningMessage);
        resetApiResult.setMessageType(STATUS_WARNING);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return resetApiResult;
    }


    public static ResetApiResult sucessWithInfo(Object data, String tips){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data==null?new Object():data);
        resetApiResult.setMessage(tips);
        resetApiResult.setMessageType(STATUS_SUCCESS);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return resetApiResult;
    }

    public static ResetApiResult authFailed(Object data, String errorMessage){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data==null?new Object():data);
        resetApiResult.setMessage(errorMessage);
        resetApiResult.setMessageType(STATUS_ERROR);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return resetApiResult;
    }
}
