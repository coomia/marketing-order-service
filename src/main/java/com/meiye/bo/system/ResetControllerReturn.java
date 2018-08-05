package com.meiye.bo.system;

import lombok.Data;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
@Data
public class ResetControllerReturn {
    private String status;
    private Integer statusCode;
    private String errorMessage;
    private Object data;

    public static final String STATUS_SUCCESS="sucess";
    public static final String STATUS_ERROR="error";
    public static final String STATUS_WARNING="warning";

    public static final int STATUS_CODE_200=200;
    public static final int STATUS_CODE_404=404;
    public static final int STATUS_CODE_500=500;




    public ResetControllerReturn(String status,Integer statusCode,String errorMessage,Object data){
        this.status=status==null?ResetControllerReturn.STATUS_SUCCESS:status;
        this.statusCode=statusCode==null?(ResetControllerReturn.STATUS_SUCCESS.equalsIgnoreCase(this.status)?ResetControllerReturn.STATUS_CODE_200:ResetControllerReturn.STATUS_CODE_500):statusCode;
        this.errorMessage=errorMessage;
        this.data=data;
    }

}
