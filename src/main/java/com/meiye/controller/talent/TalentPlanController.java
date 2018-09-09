package com.meiye.controller.talent;

import com.meiye.bo.role.AuthRoleBo;
import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.exception.BusinessException;
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
       // try {
            return ResetApiResult.sucess(talentPlanService.getOne(id));
       // }catch (Exception e){
       //     throw new BusinessException("获取人效计划失败!");
      //  }
    }

    @PostMapping("/save")
    public ResetApiResult save(@RequestBody TalentPlanBo talentPlanBo){
        try {
            talentPlanService.save(talentPlanBo);
        }catch (Exception e){
            throw new BusinessException("保存人效计划失败!");
        }
        return ResetApiResult.sucess("");
    }

    @PostMapping("/update")
    public ResetApiResult update(@RequestBody TalentPlanBo talentPlanBo){
        try {
            talentPlanService.update(talentPlanBo);
        }catch (Exception e){
            throw new BusinessException("更新人效计划失败!");
        }
        return ResetApiResult.sucess("");
    }

    @GetMapping("/delete/{id}")
    public ResetApiResult delete(@PathVariable Long id){
        try {
            talentPlanService.delete(id);
        }catch (Exception e){
            throw new BusinessException("删除人效计划失败!");
        }
        return ResetApiResult.sucess("");
    }

    @PostMapping("/getTalentPage")
    public ResetApiResult getUserPageByCriteria(@RequestBody TalentPlanBo talentPlanBo){
        try {
            return ResetApiResult.sucess(talentPlanService.getTalentPageByCriteria(talentPlanBo.getPageNum(),talentPlanBo.getPageSize(),talentPlanBo));
        }catch (Exception e){
            throw new BusinessException("获取人效计划分页失败!");
        }
    }

    @GetMapping("/status/{id}/{enabledFlag}")
    public ResetApiResult changeStatus(@PathVariable Long id, @PathVariable Integer enabledFlag){
        try {
            talentPlanService.changeStatus(id,enabledFlag);
        }catch (Exception e){
            throw new BusinessException("启用/停用 人效计划失败!");
        }
        return ResetApiResult.sucess("");
    }
}
