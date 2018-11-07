package com.meiye.system.config;

import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.meiye.bo.system.ApiResult;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.system.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/8/13 0013.
 */
@RestControllerAdvice
public class MeiYeControllerAdvice extends FastJsonViewResponseBodyAdvice {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public ApiResult errorHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        logger.error(ex.getMessage(),ex);
        if(ex instanceof BusinessException){
            if(WebUtil.isMsApiPath(request)) {
                response.setStatus(HttpServletResponse.SC_OK);
                BusinessException businessException = (BusinessException) ex;
                return new ResetApiResult(businessException.getMessage(), businessException.getMessageType(), ResetApiResult.STATUS_CODE_200, null);
            }else if(WebUtil.isPosApiPath(request)||WebUtil.isInternalApiPath(request)) {
                BusinessException businessException = (BusinessException) ex;
                return PosApiResult.error(null,ex.getMessage());
            }else if(WebUtil.isWechatApiPath(request)) {
                return PosApiResult.error(null,"未知错误.");
            }else{
                return ResetApiResult.error(null,"未知错误.");
            }
        }else{
            if(WebUtil.isMsApiPath(request)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return ResetApiResult.user("未知错误",ResetApiResult.STATUS_ERROR,ResetApiResult.STATUS_CODE_200,null);
            }else if(WebUtil.isPosApiPath(request)||WebUtil.isInternalApiPath(request)) {
                return PosApiResult.error(null,"系统未知错误.");
            }else if(WebUtil.isWechatApiPath(request)) {
                return PosApiResult.error(null,"未知错误.");
            }else{
                return ResetApiResult.error(null,"未知错误.");
            }
        }
    }
}
