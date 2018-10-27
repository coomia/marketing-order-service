package com.meiye.controller.InternalApi;

import com.meiye.bo.part.DishShopBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.part.BrandTypeService;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jonyh on 2018/10/24.
 */

@RestController
@RequestMapping(value = "/internal/api/dish",produces="application/json;charset=UTF-8")
public class InternalDishShopController {
    @Autowired
    DishShopService dishShopService;

    @Autowired
    BrandTypeService brandTypeService;

    @PostMapping("/getDishShopPageByCriteria")
    public ResetApiResult getDishShopPageByCriteria(@RequestBody DishShopBo dishShopBo){
        return ResetApiResult.sucess(dishShopService.getDishShopPageByCriteria(dishShopBo.getPageNum(),dishShopBo.getPageSize(),dishShopBo));
    }


    @GetMapping("/loadBrandTypes")
    public ResetApiResult loadBrandTypes(HttpServletResponse response){
        return ResetApiResult.sucess(brandTypeService.getAllDishBrandTypeList());
    }


}
