package com.meiye.system.config;

import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
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
    public ResetApiResult errorHandler(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        if(ex instanceof BusinessException){
            if(request.getRequestURI().startsWith(request.getContextPath()+"/public/api")) {
                BusinessException businessException = (BusinessException) ex;
                return new ResetApiResult(businessException.getMessage(), businessException.getMessageType(), businessException.getStatusCode(), null);
            }else if(request.getRequestURI().startsWith(request.getContextPath()+"/pos/api")) {

            }else if(request.getRequestURI().startsWith(request.getContextPath()+"/weichat/api")) {

            }
        }
        return ResetApiResult.error(null,"未知错误.");
    }
}
