package com.meiye.controller.part;

import com.meiye.bo.part.DishShopBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/public/api/part/dishShop",produces="application/json;charset=UTF-8")
public class DishShopController {

    @Autowired
    DishShopService dishShopService;

    @PostMapping("/save")
    public ResetApiResult saveDishShop(@RequestBody DishShopBo dishShopBo){
        dishShopService.saveDishShop(dishShopBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/load/{dishShopId}")
    public ResetApiResult getDishShopById(@PathVariable Long dishShopId){
        return ResetApiResult.sucess(dishShopService.getDishShopById(dishShopId));
    }


    @PostMapping("/getDishShopPageByCriteria")
    public ResetApiResult getDishShopPageByCriteria(@RequestBody DishShopBo dishShopBo){
        return ResetApiResult.sucess(dishShopService.getDishShopPageByCriteria(dishShopBo.getPageNum(),dishShopBo.getPageSize(),dishShopBo));
    }

    @PostMapping("/update")
    public ResetApiResult updateDishShop(@RequestBody DishShopBo dishShopBo){
        dishShopService.updateDishShop(dishShopBo);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/delete")
    public ResetApiResult deleteDishShop(@RequestBody Long dishShopId){
        dishShopService.deleteDishShop(dishShopId);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/group/save")
    public ResetApiResult newDishGroup(@RequestBody DishShopBo dishShopBo){
        dishShopService.saveDishShop(dishShopBo);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/group/update")
    public ResetApiResult updateDishGroup(@RequestBody DishShopBo dishShopBo){
        dishShopService.updateDishShop(dishShopBo);
        return ResetApiResult.sucess("");
    }

    @RequestMapping("/group/{dishShopId}/find")
    public ResetApiResult findDishShop(@PathVariable Long dishShopId){
        DishShopBo dishShopBo=dishShopService.getDishShopById(dishShopId);
        return ResetApiResult.sucess(dishShopBo);
    }
}
