package com.meiye.system.util;

import com.meiye.bo.user.UserBo;
import org.springframework.security.core.context.SecurityContextHolder;

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
        }else
            return null;
    }
}
