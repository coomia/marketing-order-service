package com.meiye.controller.part;

import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.part.BrandTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/8/8 0008.
 */
@RestController
@RequestMapping(value = "/public/api/part",produces="application/json;charset=UTF-8")
public class PartController {

    @Autowired
    BrandTypeService brandTypeService;

    @GetMapping("/loadBrandTypes")
    public ResetApiResult loadBrandTypes(HttpServletResponse response){
        return ResetApiResult.sucess(brandTypeService.getAllDishBrandTypeList());
    }

    @PostMapping("/saveBrandType")
    public ResetApiResult saveBrandType(@RequestBody DishBrandTypeBo dishBrandTypeBo){
        brandTypeService.saveOrUpdate(dishBrandTypeBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/deleteBrandType/{id}")
    public ResetApiResult loadBrandTypes(@PathVariable Long id){
        brandTypeService.delete(id);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/updateBrandType")
    public ResetApiResult updateBrandType(@RequestBody DishBrandTypeBo dishBrandTypeBo){
        brandTypeService.update(dishBrandTypeBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/getBrandTypeById/{id}")
    public ResetApiResult addBrandType(@PathVariable Long id){
        return ResetApiResult.sucess(brandTypeService.getDishBrandType(id));
    }

}
