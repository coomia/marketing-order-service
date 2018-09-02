package com.meiye.bo.sync;

import lombok.Data;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/9/1 0001.
 */
@Data
public class MeiYePosSyncParams {
    private String appType;
    private HashMap<String,HashMap<String,String>> content;
    private Integer syncCount;
    private Integer isInit;
    private String versionCode;
    private String versionName;
}
