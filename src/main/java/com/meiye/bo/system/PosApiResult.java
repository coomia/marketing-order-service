package com.meiye.bo.system;

import com.meiye.system.util.WebUtil;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/30 0030.
 */
@Data
public class PosApiResult extends ApiResult {
    private Integer status=1000;
    private String message;
    private String errors;
    private String messageId;
    private Object content;

    public PosApiResult(String message,String error,Integer statusCode,Object data){
        this.setContent(data==null?new Object():data);
        this.setMessage(message);
        this.setErrors(error);
        this.setMessageId(WebUtil.getPosRequestMessagId());
        this.setStatus(statusCode==null?1000:statusCode);
    }

    public static PosApiResult sucess(Object data){
        PosApiResult posApiResult=new PosApiResult("","",1000,data);
        return posApiResult;
    }

    public static PosApiResult error(Object data, String errorMessage){
        PosApiResult posApiResult=new PosApiResult(errorMessage,"",1001,data);
        return posApiResult;
    }

    public static PosApiResult error(Object data,Integer statusCode, String errorMessage){
        PosApiResult posApiResult=new PosApiResult(errorMessage,"",statusCode,data);
        return posApiResult;
    }
}
