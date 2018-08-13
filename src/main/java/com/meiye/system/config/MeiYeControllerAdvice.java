package com.meiye.system.config;

import com.meiye.bo.system.ResetApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by Administrator on 2018/8/13 0013.
 */
@RestControllerAdvice
public class MeiYeControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    public ResetApiResult errorHandler(Exception ex) {
        return ResetApiResult.error(null,ex.getMessage());
    }
}
