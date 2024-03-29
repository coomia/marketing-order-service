package com.meiye.system.config.mybatis;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Map;

/**
 * Created by Administrator on 2018/9/1 0001.
 */
public class MeiYeMapWrapper extends MapWrapper {


    public MeiYeMapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }


    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if(useCamelCaseMapping){
            //CaseFormat是引用的 guava库,里面有转换驼峰的,免得自己重复造轮子,pom添加
            /**
             **         <dependency>
             <groupId>com.google.guava</groupId>
             <artifactId>guava</artifactId>
             <version>24.1-jre</version>
             </dependency>
             **/
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,name);
        }
        return name;
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        if(value!=null)
            super.set(prop, value);
        else
            super.set(prop,"");
    }
}
