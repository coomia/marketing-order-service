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

    @PostMapping("/delete/{id}")
    public ResetApiResult delete(@PathVariable Long id){
        dishPropertyService.deleteById(id);
        return ResetApiResult.sucess("");
    }
}
