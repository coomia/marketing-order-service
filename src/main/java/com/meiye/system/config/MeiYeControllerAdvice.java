package com.meiye.system.config;

import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.meiye.bo.system.ResetApiResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by Administrator on 2018/8/13 0013.
 */
@RestControllerAdvice
public class MeiYeControllerAdvice extends FastJsonViewResponseBodyAdvice {
    @ExceptionHandler(value = Exception.class)
    public ResetApiResult errorHandler(Exception ex) {
        return ResetApiResult.error(null,ex.getMessage());
    }
}
