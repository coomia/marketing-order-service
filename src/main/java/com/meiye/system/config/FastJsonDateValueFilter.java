package com.meiye.system.config;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2018/9/26 0026.
 */
public class FastJsonDateValueFilter implements ValueFilter {
    @Override
    public Object process(Object o, String s, Object o1) {
        if(o1==null) {
            try {
                Class filedClass=null;
                try {
                    filedClass = o.getClass().getDeclaredField(s).getType();
                }catch (NoSuchFieldException exp){
                    filedClass=o.getClass().getSuperclass().getDeclaredField(s).getType();
                }
                if(filedClass.equals(Timestamp.class)||filedClass.equals(Date.class)||filedClass.equals(java.sql.Date.class))
                    return 0;
            }catch (Exception exp){
            }
        }
        return o1;
    }
}
