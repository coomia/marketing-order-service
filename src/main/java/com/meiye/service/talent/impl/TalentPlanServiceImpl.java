package com.meiye.service.talent.impl;

import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.bo.talent.TalentRoleBo;
import com.meiye.bo.talent.TalentRuleBo;
import com.meiye.model.role.AuthUser;
import com.meiye.model.talent.TalentPlan;
import com.meiye.model.talent.TalentRole;
import com.meiye.model.talent.TalentRule;
import com.meiye.repository.talent.TalentPlanRepository;
import com.meiye.repository.talent.TalentRoleRepository;
import com.meiye.repository.talent.TalentRuleRepository;
import com.meiye.service.talent.TalentPlanService;
import com.meiye.system.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
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

    @Transactional(rollbackOn = {Exception.class})
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
        if(talentPlanBo.getTalentRuleBos() != null && talentPlanBo.getTalentRuleBos().size()>0){
            talentPlanBo.getTalentRuleBos().forEach(talentRuleBo->{
                TalentRule talentRule = talentRuleBo.copyTo(TalentRule.class);
                talentRule.setPlanId(talentPlan.getId());
                talentRuleRepository.save(talentRule);
            });
        }
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void update(TalentPlanBo talentPlanBo) {
        if(talentPlanBo ==null){
            return;
        }
        //更新效方案
        TalentPlan talentPlan = talentPlanBo.copyTo(TalentPlan.class);
        talentPlanRepository.save(talentPlan);
        //更新效角色 -- 先全部删除再save
        talentRoleRepository.deleteByPlanId(talentPlanBo.getId());
        if( talentPlanBo.getTalentRoleBoList().size()>0){
            talentPlanBo.getTalentRoleBoList().forEach(talentRoleBo->{
                TalentRole talentRole = talentRoleBo.copyTo(TalentRole.class);
                talentRole.setPlanId(talentPlan.getId());
                talentRoleRepository.save(talentRole);
            });
        }
        //更新提成规则  -- 先全部删除再save
        talentRuleRepository.deleteByPlanId(talentPlanBo.getId());
        if(talentPlanBo.getTalentRuleBos() != null && talentPlanBo.getTalentRuleBos().size()>0){
            talentPlanBo.getTalentRuleBos().forEach(talentRuleBo->{
                TalentRule talentRule = talentRuleBo.copyTo(TalentRule.class);
                talentRule.setPlanId(talentPlan.getId());
                talentRuleRepository.save(talentRule);
            });
        }
    }

    @Transactional(rollbackOn = {Exception.class})
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
        TalentPlan talentPlan = talentPlanRepository.findByIdAndStatusFlag(id,1);
        if(talentPlan != null){
            TalentPlanBo talentPlanBo = talentPlan.copyTo(TalentPlanBo.class);
            //得到人效角色
            List<TalentRole> talentRolesByPlanId = talentRoleRepository.getTalentRolesByPlanIdAndStatusFlag(id,1);
            if(talentRolesByPlanId != null && talentRolesByPlanId.size()>0){
                List<TalentRoleBo> TalentRuleBos = new ArrayList<>();
                talentRolesByPlanId.forEach(talentRole->{
                    TalentRoleBo talentRoleBo = talentRole.copyTo(TalentRoleBo.class);
                    TalentRuleBos.add(talentRoleBo);
                });
                talentPlanBo.setTalentRoleBoList(TalentRuleBos);
            }
            //得到人效规则
            List<TalentRule> talentRules = talentRuleRepository.getTalentRuleByPlanIdAndStatusFlag(id,1);
            if(talentRules != null && talentRules.size()>0){
                List<TalentRuleBo> talentRuleBos = new ArrayList<TalentRuleBo>();
                talentRules.forEach(talentRule->{
                    TalentRuleBo talentRuleBo = talentRule.copyTo(TalentRuleBo.class);
                    talentRuleBos.add(talentRuleBo);
                });
                talentPlanBo.setTalentRuleBos(talentRuleBos);
            }
            return talentPlanBo;
        }
        return null;
    }

    @Override
    public Page<TalentPlan> getTalentPageByCriteria(Integer pageNum, Integer pageSize, TalentPlanBo talentPlanBo) {
        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id");
        Page<TalentPlan> usersPage = talentPlanRepository.findAll(new Specification<TalentPlan>() {
            @Override
            public Predicate toPredicate(Root<TalentPlan> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (null != talentPlanBo.getPlanType()) {
                    list.add(criteriaBuilder.equal(root.get("planType").as(Long.class), talentPlanBo.getPlanType()));
                }
                if (null != talentPlanBo.getEnabledFlag()) {
                    list.add(criteriaBuilder.equal(root.get("enabledFlag").as(String.class), talentPlanBo.getEnabledFlag()));
                }
                list.add(criteriaBuilder.equal(root.get("statusFlag").as(Long.class), 1));
                list.add(criteriaBuilder.equal(root.get("brandIdenty").as(Long.class), WebUtil.getCurrentBrandId()));
                list.add(criteriaBuilder.equal(root.get("shopIdenty").as(Long.class), WebUtil.getCurrentStoreId()));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return usersPage;
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void changeStatus(Long id, Integer enabledFlag) {
        talentPlanRepository.changeStatus(id,enabledFlag);
    }
}
