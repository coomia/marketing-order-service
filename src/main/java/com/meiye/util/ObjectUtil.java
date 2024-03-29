package com.meiye.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class ObjectUtil {
    private static final String JAVAP = "java.";
    private static final String JAVADATESTR = "java.util.Date";

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj,boolean excludeNullValue) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if(!excludeNullValue||value!=null)
                map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 利用递归调用将Object中的值全部进行获取
     *
     * @param timeFormatStr 格式化时间字符串默认<strong>2017-03-10 10:21</strong>
     * @param obj           对象
     * @param excludeFields 排除的属性
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMapString(String timeFormatStr, Object obj,boolean excludeNullValue, String... excludeFields) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();

        if (excludeFields.length!=0){
            List<String> list = Arrays.asList(excludeFields);
            objectTransfer(timeFormatStr, obj, map, list,excludeNullValue);
        }else{
            objectTransfer(timeFormatStr, obj, map,null,excludeNullValue);
        }
        return map;
    }


    /**
     * 递归调用函数
     *
     * @param obj           对象
     * @param map           map
     * @param excludeFields 对应参数
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, String> objectTransfer(String timeFormatStr, Object obj, Map<String, String> map, List<String> excludeFields,boolean excludeNullValue) throws IllegalAccessException {
        boolean isExclude=false;
        //默认字符串
        String formatStr = "YYYY-MM-dd HH:mm:ss";
        //设置格式化字符串
        if (timeFormatStr != null && !timeFormatStr.isEmpty()) {
            formatStr = timeFormatStr;
        }
        if (excludeFields!=null){
            isExclude=true;
        }
        Class<?> clazz = obj.getClass();
        //获取值
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName =  field.getName();
            //判断是不是需要跳过某个属性
            if (isExclude&&excludeFields.contains(fieldName)){
                continue;
            }
            //设置属性可以被访问
            field.setAccessible(true);
            Object value = field.get(obj);
            if(!excludeNullValue||value!=null) {
                if (value == null)
                    map.put(fieldName, "");
                else {
                    Class<?> valueClass = value.getClass();
                    if (valueClass.isPrimitive()) {
                        map.put(fieldName, value.toString());

                    } else if (valueClass.getName().contains(JAVAP)) {//判断是不是基本类型
                        if (valueClass.getName().equals(JAVADATESTR)) {
                            //格式化Date类型
                            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                            Date date = (Date) value;
                            String dataStr = sdf.format(date);
                            map.put(fieldName, dataStr);
                        } else {
                            map.put(fieldName, value.toString());
                        }
                    } else {
                        objectTransfer(timeFormatStr, value, map, excludeFields, excludeNullValue);
                    }
                }
            }
        }
        return map;
    }

    public static boolean equals(Object object1, Object object2) {
        return object1 == object2?true:(object1 != null && object2 != null?object1.equals(object2):false);
    }

    public static boolean notEqual(Object object1, Object object2) {
        return !equals(object1, object2);
    }

}
