package com.meiye.controller.part;

import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.part.BrandTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/8/8 0008.
 */
@RestController
@RequestMapping(value = "/public/api/part",produces="application/json;charset=UTF-8")
public class PartController {

    @Autowired
    BrandTypeService brandTypeService;

    @RequestMapping("/loadBrandTypes")
    public ResetApiResult loadBrandTypes(HttpServletResponse response){
        return ResetApiResult.sucess(brandTypeService.getAllDishBrandTypeList());
    }
}
