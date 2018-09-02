package com.meiye.system.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.meiye.bo.store.CommercialBo;
import com.meiye.bo.store.StoreBo;
import com.meiye.bo.user.UserBo;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by Administrator on 2018/8/9 0009.
 */
public class WebUtil {
    public static UserBo getCurrentUser(){
        if(Objects.isNull(SecurityContextHolder.getContext().getAuthentication()))
            return null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof UserBo) {
            UserBo user = (UserBo) principal;
            return user;
        }else {
            return null;
        }
    }

    public static CommercialBo getCurrentStore(){
        UserBo userBo=getCurrentUser();
        if(userBo!=null)
            return userBo.getStoreBo();
        else
            return null;
    }

    public static Long getCurrentStoreId(){
        CommercialBo storeBo=getCurrentStore();
        return storeBo==null?null:storeBo.getCommercialId();
    }

    public static Long getCurrentBrandId(){
        return new Long(1);
    }

    public static SerializerFeature[] getFastJsonSerializerFeature(){
        return new SerializerFeature[]{SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteNullNumberAsZero};
    }


    public static void setRestResponseHeader(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","token, Content-Type");
        httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
    }

    //是否后台管理系统的路径
    public static boolean isMsApiPath(HttpServletRequest request){
        return request.getRequestURI().startsWith(request.getContextPath()+"/public/api");
    }

    //是否Pos端API
    public static boolean isPosApiPath(HttpServletRequest request){
        return request.getRequestURI().startsWith(request.getContextPath()+"/pos/api");
    }

    //是否微信客户端API
    public static boolean isWechatApiPath(HttpServletRequest request){
        return request.getRequestURI().startsWith(request.getContextPath()+"/weichat/api");
    }

    public static String getPosRequestHeaderPrefix(){
        return "";
    }

    public static String getPosRequestMessagId(){
        UserBo userBo=getCurrentUser();
        if(userBo!=null)
            return userBo.getRequestMsgId();
        else
            return null;
    }
}
