package com.meiye.controller.part;

import com.meiye.bo.part.DishShopBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/public/api/part/dishShop",produces="application/json;charset=UTF-8")
public class DishShopController {

    @Autowired
    DishShopService dishShopService;

    @RequestMapping("/save")
    public ResetApiResult saveDishShop(DishShopBo dishShopBo){
        dishShopService.saveDishShop(dishShopBo);
        return ResetApiResult.sucess("");
    }

    @RequestMapping("/load")
    public ResetApiResult getDishShopById(Long shopId){
        dishShopService.getDishShopById(shopId);
        return ResetApiResult.sucess("");
    }

    @RequestMapping("/update")
    public ResetApiResult updateDishShop(DishShopBo dishShopBo){
        return ResetApiResult.sucess("");
    }

    @RequestMapping("/delete")
    public ResetApiResult deleteDishShop(Long dishShopId){
        return ResetApiResult.sucess("");
    }

}
