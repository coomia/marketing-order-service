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

    @GetMapping("/load")
    public ResetApiResult getDishShopById(@RequestParam Long shopId){
        dishShopService.getDishShopById(shopId);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/update")
    public ResetApiResult updateDishShop(@RequestBody DishShopBo dishShopBo){
        return ResetApiResult.sucess("");
    }

    @PostMapping("/delete")
    public ResetApiResult deleteDishShop(@RequestBody Long dishShopId){
        return ResetApiResult.sucess("");
    }

}
