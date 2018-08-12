package com.meiye.bo.system;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
@Data
public class ResetApiResult {
    private Integer statusCode;
    private String message;
    private String messageType;
    private Object data;

    public static final String STATUS_SUCCESS="info";
    public static final String STATUS_ERROR="error";
    public static final String STATUS_WARNING="warning";
    public static final String STATUS_IGNORE="ignore";

    public static final int STATUS_CODE_200=200;
    public static final int STATUS_CODE_404=404;
    public static final int STATUS_CODE_403=403;
    public static final int STATUS_CODE_500=500;

    public static String sucess(Object data){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data);
        resetApiResult.setMessage("");
        resetApiResult.setMessageType(STATUS_IGNORE);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return JSON.toJSONString(resetApiResult);
    }

    public static String error(Object data, String errorMessage){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data);
        resetApiResult.setMessage(errorMessage);
        resetApiResult.setMessageType(STATUS_ERROR);
        resetApiResult.setStatusCode(STATUS_CODE_500);
        return JSON.toJSONString(resetApiResult);
    }


    public static String sucessWithWarning(Object data, String warningMessage){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data);
        resetApiResult.setMessage(warningMessage);
        resetApiResult.setMessageType(STATUS_WARNING);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return JSON.toJSONString(resetApiResult);
    }


    public static String sucessWithInfo(Object data, String tips){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data);
        resetApiResult.setMessage(tips);
        resetApiResult.setMessageType(STATUS_SUCCESS);
        resetApiResult.setStatusCode(STATUS_CODE_200);
        return JSON.toJSONString(resetApiResult);
    }

    public static String authFailed(Object data, String errorMessage){
        ResetApiResult resetApiResult =new ResetApiResult();
        resetApiResult.setData(data);
        resetApiResult.setMessage(errorMessage);
        resetApiResult.setMessageType(STATUS_ERROR);
        resetApiResult.setStatusCode(STATUS_CODE_403);
        return JSON.toJSONString(resetApiResult);
    }
}
