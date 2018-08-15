package com.meiye.controller.part;

import com.meiye.bo.part.DishPropertyBo;
import com.meiye.bo.part.DishShopBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.model.part.DishProperty;
import com.meiye.service.part.DishPropertyService;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/public/api/part/dishProperty",produces="application/json;charset=UTF-8")
public class DishPropertyController {

    @Autowired
    DishPropertyService dishPropertyService;

    @PostMapping("/save")
    public ResetApiResult save(@RequestBody DishPropertyBo dishPropertyBo){
        //dishPropertyService.save
        return ResetApiResult.sucess("");
    }

    @GetMapping("/load")
    public ResetApiResult getById(@RequestParam Long shopId){
        //dishPropertyService.get
        return ResetApiResult.sucess("");
    }

    @PostMapping("/update")
    public ResetApiResult update(@RequestBody DishPropertyBo dishPropertyBo){
        return ResetApiResult.sucess("");
    }

    @PostMapping("/delete")
    public ResetApiResult delete(@RequestBody Long dishShopId){
        return ResetApiResult.sucess("");
    }
}
