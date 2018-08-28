package com.meiye.controller.role;

import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.model.role.AuthPermission;
import com.meiye.service.role.AuthPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/8/28 22:55
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/public/api/role/authPermission",produces="application/json;charset=UTF-8")
public class AuthPermissionController {
    @Autowired
    AuthPermissionService authPermissionService;

    @GetMapping("/findAll")
    public ResetApiResult findAll(){
        List<AuthPermission> all;
        try {
            all = authPermissionService.getAll();
        }catch (Exception e){
            throw new BusinessException("获取所有权限失败!");
        }
        return ResetApiResult.sucess(all);
    }

}
