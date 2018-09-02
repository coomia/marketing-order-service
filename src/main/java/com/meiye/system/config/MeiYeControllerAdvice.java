package com.meiye.system.config;

import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.meiye.bo.system.ApiResult;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.system.util.WebUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/8/13 0013.
 */
@RestControllerAdvice
public class MeiYeControllerAdvice extends FastJsonViewResponseBodyAdvice {
    @ExceptionHandler(value = Exception.class)
    public ApiResult errorHandler(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        if(ex instanceof BusinessException){
            if(WebUtil.isMsApiPath(request)) {
                BusinessException businessException = (BusinessException) ex;
                return new ResetApiResult(businessException.getMessage(), businessException.getMessageType(), businessException.getStatusCode(), null);
            }else if(WebUtil.isPosApiPath(request)) {
                BusinessException businessException = (BusinessException) ex;
                return PosApiResult.error(null,ex.getMessage());
            }else if(WebUtil.isWechatApiPath(request)) {
                return ResetApiResult.error(null,"未知错误.");
            }else{
                return ResetApiResult.error(null,"未知错误.");
            }
        }else{
            if(WebUtil.isMsApiPath(request)) {
                return ResetApiResult.error(null,"未知错误.");
            }else if(WebUtil.isPosApiPath(request)) {
                return PosApiResult.error(null,"系统未知错误.");
            }else if(WebUtil.isWechatApiPath(request)) {
                return ResetApiResult.error(null,"未知错误.");
            }else{
                return ResetApiResult.error(null,"未知错误.");
            }
        }
    }
}
