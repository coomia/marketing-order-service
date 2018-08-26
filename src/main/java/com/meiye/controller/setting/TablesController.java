package com.meiye.controller.setting;

import com.meiye.bo.setting.TableAreaBo;
import com.meiye.bo.setting.TablesBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.service.setting.TableAreaService;
import com.meiye.service.setting.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:56 2018/8/26
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/public/api/setting/table",produces="application/json;charset=UTF-8")
public class TablesController {

    @Autowired
    TableService tableService;

    @GetMapping("/loadTablesByAreaId/{areaId}")
    public ResetApiResult loadTablesByAreaId(@PathVariable Long areaId){
        return ResetApiResult.sucess(tableService.getAllTablesByAreaId(areaId));
    }

    @PostMapping("/saveTable")
    public ResetApiResult saveTable(@RequestBody TablesBo tablesBo){
        tableService.addTable(tablesBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/deleteTable/{id}")
    public ResetApiResult deleteTable(@PathVariable Long id){
        tableService.deleteTable(id);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/updateTable")
    public ResetApiResult updateTable(@RequestBody TablesBo tablesBo){
        tableService.updateTable(tablesBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/getTableCountByAreaId/{areaId}")
    public ResetApiResult getTableCountByAreaId(@PathVariable Long areaId){
        tableService.getTablesCountByAreaId(areaId);
        return ResetApiResult.sucess("");
    }


}