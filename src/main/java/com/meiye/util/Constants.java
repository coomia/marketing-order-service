package com.meiye.util;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:40 2018/9/1
 * @Modified By:
 */
public class Constants {

    public static String APP_KEY = "app";
    public static String STORE_KEY = "store";
    public static String MEMBER_KEY = "member";
    public static Long STORE_SETTING_TYPE = new Long(1);
    public static Long APP_SETTING_TYPE =  new Long(2);
    public static Long MEMBER_SETTING_TYPE =  new Long(3);

    public static Integer DATA_ENABLE = 1;
    public static Integer DATA_DISABLE = 2;

    public static Integer SELF_TABLE_STATUS_UNLOCK = 1;
    public static Integer SELF_TABLE_STATUS_LOCK = 2;

    public static Integer NOT_TO_THE_STORE__STATUS = -1;//未到店
    public static Integer SUCCESS_STATUS = 2;//已离店(开单)
    public static Integer CANCEL_STATUS = 9;//已取消
    public static Integer PENNDING_OPERATE_STATUS = -2;//未处理
    public static Integer ARRIVE_THE_STORE__STATUS = 1;//未到店

}
