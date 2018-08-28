package com.meiye.controller.part;

import com.meiye.bo.part.DishShopBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
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
        try{
            dishShopService.saveDishShop(dishShopBo);
        }catch (Exception e){
            throw new BusinessException("品项保存失败!");
        }

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
        try {
            dishShopService.updateDishShop(dishShopBo);
        }catch (Exception e){
            throw new BusinessException("品项更新失败!");
        }

        return ResetApiResult.sucess("");
    }

    @PostMapping("/delete")
    public ResetApiResult deleteDishShop(@RequestBody Long dishShopId){
        try {
            dishShopService.deleteDishShop(dishShopId);
        }catch (Exception e){
            throw new BusinessException("品项删除失败!");
        }

        return ResetApiResult.sucess("");
    }

    @PostMapping("/group/save")
    public ResetApiResult newDishGroup(@RequestBody DishShopBo dishShopBo){
        Long id=dishShopService.saveDishShop(dishShopBo);
        if(id!=null)
            return findDishShop(id);
        else
            return ResetApiResult.error("","保存失败");
    }

    @PostMapping("/group/update")
    public ResetApiResult updateDishGroup(@RequestBody DishShopBo dishShopBo){
        Long id=dishShopService.updateDishShop(dishShopBo);
        if(id!=null)
            return findDishShop(id);
        else
            return ResetApiResult.error("","保存失败");
    }

    @RequestMapping("/group/{dishShopId}/find")
    public ResetApiResult findDishShop(@PathVariable Long dishShopId){
        DishShopBo dishShopBo=dishShopService.getDishShopById(dishShopId);
        return ResetApiResult.sucess(dishShopBo);
    }
}
