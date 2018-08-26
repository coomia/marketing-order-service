package com.meiye.service.talent.impl;

import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.bo.talent.TalentRoleBo;
import com.meiye.bo.talent.TalentRuleBo;
import com.meiye.model.talent.TalentPlan;
import com.meiye.model.talent.TalentRole;
import com.meiye.model.talent.TalentRule;
import com.meiye.repository.talent.TalentPlanRepository;
import com.meiye.repository.talent.TalentRoleRepository;
import com.meiye.repository.talent.TalentRuleRepository;
import com.meiye.service.talent.TalentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TalentPlanServiceImpl implements TalentPlanService{
    @Autowired
    TalentPlanRepository talentPlanRepository;

    @Autowired
    TalentRoleRepository talentRoleRepository;

    @Autowired
    TalentRuleRepository talentRuleRepository;

    @Override
    public void save(TalentPlanBo talentPlanBo) {
        if(talentPlanBo ==null){
            return;
        }
        //存人效方案
        TalentPlan talentPlan = talentPlanBo.copyTo(TalentPlan.class);
        talentPlanRepository.save(talentPlan);
        //存人效角色
        if( talentPlanBo.getTalentRoleBoList().size()>0){
            talentPlanBo.getTalentRoleBoList().forEach(talentRoleBo->{
                TalentRole talentRole = talentRoleBo.copyTo(TalentRole.class);
                talentRole.setPlanId(talentPlan.getId());
                talentRoleRepository.save(talentRole);
            });
        }
        //存提成规则
        if(talentPlanBo.getTalentRuleBo() != null){
            TalentRuleBo talentRuleBo = talentPlanBo.getTalentRuleBo();
            TalentRule talentRule = talentRuleBo.copyTo(TalentRule.class);
            talentRule.setPlanId(talentPlan.getId());
            talentRuleRepository.save(talentRule);
        }
    }

    @Override
    public void update(TalentPlanBo talentPlanBo) {
        if(talentPlanBo ==null){
            return;
        }
        //更新效方案
        TalentPlan talentPlan = talentPlanBo.copyTo(TalentPlan.class);
        talentPlanRepository.save(talentPlan);
        //更新效角色 -- 先全部删除再save
        talentRuleRepository.deleteByPlanId(talentPlanBo.getId());
        if( talentPlanBo.getTalentRoleBoList().size()>0){
            talentPlanBo.getTalentRoleBoList().forEach(talentRoleBo->{
                TalentRole talentRole = talentRoleBo.copyTo(TalentRole.class);
                talentRole.setPlanId(talentPlan.getId());
                talentRoleRepository.save(talentRole);
            });
        }
        //更新提成规则
        if(talentPlanBo.getTalentRuleBo() != null){
            TalentRuleBo talentRuleBo = talentPlanBo.getTalentRuleBo();
            TalentRule talentRule = talentRuleBo.copyTo(TalentRule.class);
            talentRule.setPlanId(talentPlan.getId());
            talentRuleRepository.save(talentRule);
        }
    }

    @Override
    public void delete(Long id) {
        talentPlanRepository.delete(id);
    }

    @Override
    public TalentPlanBo getOne(Long id) {
        if(id == null){
            return null;
        }
        //得到人效计划
        TalentPlan talentPlan = talentPlanRepository.getOne(id);
        if(talentPlan != null){
            TalentPlanBo talentPlanBo = talentPlan.copyTo(TalentPlanBo.class);
            //得到人效角色
            List<TalentRole> talentRolesByPlanId = talentRoleRepository.getTalentRolesByPlanId(id);
            if(talentRolesByPlanId != null && talentRolesByPlanId.size()>0){
                List<TalentRoleBo> TalentRuleBos = new ArrayList<>();
                talentRolesByPlanId.forEach(talentRole->{
                    TalentRoleBo talentRoleBo = talentRole.copyTo(TalentRoleBo.class);
                    TalentRuleBos.add(talentRoleBo);
                });
                talentPlanBo.setTalentRoleBoList(TalentRuleBos);
            }
            //得到人效规则
            TalentRule talentRule = talentRuleRepository.getTalentRuleByPlanId(id);
            if(talentRule != null){
                TalentRuleBo talentRuleBo = talentRule.copyTo(TalentRuleBo.class);
                talentPlanBo.setTalentRuleBo(talentRuleBo);
            }
            return talentPlanBo;
        }
        return null;
    }
}
