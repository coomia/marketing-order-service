package com.meiye.bo.system;

import lombok.Data;

/**
 * Created by jonyh on 2018/11/22.
 */
@Data
public class PosSyncApiResult extends PosApiResult {
    public PosSyncApiResult(String message, String error, Integer statusCode, Object data) {
        super(message, error, statusCode, data);
    }
    public static Integer SYNC_COMPLETE=0;
    public static Integer SYNC_UNCOMPLETE=1;


    private Integer lastSyncStatus;


    public static PosApiResult sucess(Object data,Integer syncComplete){
        PosSyncApiResult posApiResult=new PosSyncApiResult("","",1000,data);
        posApiResult.setLastSyncStatus(syncComplete);
        return posApiResult;
    }
}
