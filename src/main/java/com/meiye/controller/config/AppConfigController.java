package com.meiye.controller.config;

import com.meiye.annotation.CurrentUser;
import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.user.UserBo;
import com.meiye.service.config.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/8 0008.
 */
@Controller
@RequestMapping(path = "/web")
public class AppConfigController {

    @Autowired
    AppConfigService appConfigService;

    @PostMapping(path = "/config/new")
    public String newConfig(@RequestBody AppConfigBo appConfigBo, @CurrentUser UserBo userBo){
        if(appConfigBo!=null){
            appConfigBo.setCreatorId(userBo.getId());
            Date now=new Date();
            appConfigBo.setStatusFlag(1);
            appConfigBo.setServerCreateTime(now);
            appConfigBo.setUpdatorId(userBo.getId());
            appConfigBo.setServerUpdateTime(now);
            appConfigService.saveConfig(appConfigBo);
            return "index";
        }else{
            return "";
        }
    }
}
