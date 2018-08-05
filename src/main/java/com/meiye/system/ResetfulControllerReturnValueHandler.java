package com.meiye.system;

import com.meiye.bo.system.ResetControllerReturn;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
public class ResetfulControllerReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return true;
    }

    @Override
    public void handleReturnValue(@Nullable Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        if(o!=null && o instanceof ResetControllerReturn)
            return;
        else
            o=new ResetControllerReturn(ResetControllerReturn.STATUS_SUCCESS,ResetControllerReturn.STATUS_CODE_200,"",o);
    }
}
