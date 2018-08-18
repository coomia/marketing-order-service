package com.meiye.controller.config;

import com.meiye.annotation.CurrentUser;
import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.user.UserBo;
import com.meiye.service.config.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
@RestController
@RequestMapping(path = "/public/api/appConfig")
public class AppConfigRestController {
    @Autowired
    AppConfigService appConfigService;

    @PostMapping(path = "/new")
    public void newConfig(@RequestBody AppConfigBo appConfigBo, @CurrentUser UserBo userBo){
        if(appConfigBo!=null){
            appConfigBo.setCreatorId(userBo.getId());
            Date now=new Date();
            appConfigBo.setStatusFlag(1);
            appConfigBo.setServerCreateTime(now);
            appConfigBo.setUpdatorId(userBo.getId());
            appConfigBo.setServerUpdateTime(now);
            appConfigService.saveConfig(appConfigBo);
        }else{
            return;
        }
    }

    @GetMapping(path = "/list")
    public ResetApiResult loadAppConfig(){
        List<AppConfigBo> appConfigBos=appConfigService.listAppConfigs();
        return ResetApiResult.sucess(appConfigBos);
    }


    @GetMapping(path = "/get/{configId}")
    public AppConfigBo loadAppConfig(@PathVariable Long configId){
        AppConfigBo appConfigBo=appConfigService.getAppConfig(configId);
        return appConfigBo;
    }
}
