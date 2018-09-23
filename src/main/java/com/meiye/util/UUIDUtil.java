package com.meiye.util;

import java.util.UUID;

/**
 * Created by jonyh on 2016/7/22.
 */
public class UUIDUtil {
    public static String randomUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
