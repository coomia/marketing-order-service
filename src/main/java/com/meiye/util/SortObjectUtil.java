package com.meiye.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/9 0009.
 */
public class SortObjectUtil {
    public static String getSortString(Object object,String[] excludeProperties,boolean excludeNullValue){
        StringBuffer results=new StringBuffer();
        if(object==null)
            return null;
        if(object instanceof Map)
            return getSortStringForMap((Map<String,String>)object,excludeProperties,excludeNullValue);
        Field[] fields=ReflectionUtil.getNonStaticFields(object.getClass());
        if(fields==null||fields.length<1)
            return null;
        String[] fieldNames=new String[fields.length];
        int arrayIndex=0;
        for(Field field:fields){
            fieldNames[arrayIndex++]=field.getName();
        }
        Arrays.sort(fieldNames);
        for(String fieldName:fieldNames){
            if(isArrayContainsValue(excludeProperties,fieldName))
                continue;
            Object filedValue=ReflectionUtil.invokeGetter(object,fieldName);
            if(!excludeNullValue||filedValue!=null) {
                results.append(fieldName.toLowerCase());
                results.append("=");
                results.append(filedValue == null ? "" : filedValue.toString());
                results.append("&");
            }
        }
        if('&'==results.charAt(results.length()-1))
            results.deleteCharAt(results.length()-1);

        return results.toString();
    }

    private static String getSortStringForMap(Map<String,String> objectMap,String[] excludeProperties,boolean excludeNullValue){
        StringBuffer results=new StringBuffer();
        if(objectMap==null||objectMap.isEmpty())
            return null;
        Iterator<String> iterator=objectMap.keySet().iterator();
        Object[] keys=  objectMap.keySet().toArray();
        Arrays.sort(keys);
        for(Object fieldName:keys){
            if(isArrayContainsValue(excludeProperties,fieldName))
                continue;

            Object filedValue = objectMap.get(fieldName);
            if(!excludeNullValue||filedValue!=null) {
                results.append(fieldName.toString().toLowerCase());
                results.append("=");
                results.append(filedValue == null ? "" : filedValue.toString());
                results.append("&");
            }
        }
        if('&'==results.charAt(results.length()-1))
            results.deleteCharAt(results.length()-1);
        return results.toString();
    }

    public static String getValuesSortString(Object object,String[] excludeProperties,boolean excludeNullValue){
        StringBuffer results=new StringBuffer();
        if(object==null)
            return null;
        if(object instanceof Map)
            return getValuesSortStringForMap((Map<String,String>)object,excludeProperties,excludeNullValue);
        Field[] fields=ReflectionUtil.getNonStaticFields(object.getClass());
        if(fields==null||fields.length<1)
            return null;
        String[] fieldValues=new String[fields.length];
        int arrayIndex=0;
        for(Field field:fields){
            if(isArrayContainsValue(excludeProperties,field.getName()))
                continue;
            Object filedValue=ReflectionUtil.invokeGetter(object,field.getName());
            if(!excludeNullValue||filedValue!=null) {
                if (filedValue == null)
                    fieldValues[arrayIndex++] = "";
                else
                    fieldValues[arrayIndex++] = filedValue.toString();
            }
        }
        while (arrayIndex<fields.length){
            fieldValues[arrayIndex++]="";
        }
        Arrays.sort(fieldValues);
        for(String fieldVal:fieldValues){
            results.append(fieldVal);
        }
        return results.toString();
    }

    private static String getValuesSortStringForMap(Map<String,String> objectMap,String[] excludeProperties,boolean excludeNullValue){
        StringBuffer results=new StringBuffer();
        if(objectMap==null||objectMap.isEmpty())
            return null;
        Iterator<String> iterator=objectMap.keySet().iterator();
        String[] keys= (String[]) objectMap.keySet().toArray();
        String[] values=new String[keys.length];
        int arrayIndex=0;
        for(String fieldName:keys){
            if(isArrayContainsValue(excludeProperties,fieldName))
                continue;
            if(!excludeNullValue||objectMap.get(fieldName)!=null)
                values[arrayIndex++]=objectMap.get(fieldName);
        }
        while (arrayIndex<values.length){
            values[arrayIndex++]="";
        }
        Arrays.sort(values);
        for(String fieldVal:values){
            results.append(fieldVal);
        }
        return results.toString();
    }


    private static boolean isArrayContainsValue(Object[] array,Object value,boolean excludeNull){
        boolean isContain=false;
        if(array==null)
            return isContain;

        for(Object val:array){
            if(!excludeNull&&val==null&&value==null){
                isContain=true;
                break;
            }
            if(value==null&&excludeNull)
                break;
            if(value.equals(val)||val==value){
                isContain=true;
                break;
            }
        }
        return isContain;
    }

    private static boolean isArrayContainsValue(Object[] array,Object value){
        return isArrayContainsValue(array,value,true);
    }
}
