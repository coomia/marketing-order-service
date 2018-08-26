package com.meiye.controller.talent;

import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.service.talent.TalentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Shawns
 * @Date: 2018/8/26 22:25
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/public/api/talentPlan",produces="application/json;charset=UTF-8")
public class TalentPlanController {

    @Autowired
    TalentPlanService talentPlanService;


    @GetMapping("/find/{id}")
    public ResetApiResult getOne(@PathVariable Long id){
        return ResetApiResult.sucess(talentPlanService.getOne(id));
    }

    @PostMapping("/save")
    public ResetApiResult save(TalentPlanBo talentPlanBo){
        talentPlanService.save(talentPlanBo);
        return ResetApiResult.sucess("");
    }

    @PostMapping("/update")
    public ResetApiResult update(TalentPlanBo talentPlanBo){
        talentPlanService.update(talentPlanBo);
        return ResetApiResult.sucess("");
    }

    @GetMapping("/delete/{id}")
    public ResetApiResult delete(@PathVariable Long id){
        talentPlanService.delete(id);
        return ResetApiResult.sucess("");
    }


}
