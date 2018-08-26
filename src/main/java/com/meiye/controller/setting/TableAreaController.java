package com.meiye.controller.setting;

import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.bo.setting.TableAreaBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.model.setting.TableArea;
import com.meiye.service.setting.TableAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:56 2018/8/26
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/public/api/setting/tableArea",produces="application/json;charset=UTF-8")
public class TableAreaController {

    @Autowired
    TableAreaService tableAreaService;

    @GetMapping("/loadTableAreas")
    public ResetApiResult loadTableAreas(){
        return ResetApiResult.sucess(tableAreaService.getAllTableArea());
    }

    @PostMapping("/saveTableArea")
    public ResetApiResult saveTableArea(@RequestBody TableAreaBo tableAreaBo){
        tableAreaService.addTableArea(tableAreaBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/deleteTableArea/{id}")
    public ResetApiResult deleteTableArea(@PathVariable Long id){
        tableAreaService.deleteTableArea(id);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/updateTableArea")
    public ResetApiResult updateTableArea(@RequestBody TableAreaBo tableAreaBo){
        tableAreaService.updateTableArea(tableAreaBo);
        return ResetApiResult.sucess("");
    }


}
