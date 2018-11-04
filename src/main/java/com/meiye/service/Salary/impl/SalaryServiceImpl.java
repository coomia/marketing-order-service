package com.meiye.service.Salary.impl;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.salary.ProjectCommionsDetailBo;
import com.meiye.bo.salary.SalaryBo;
import com.meiye.bo.salary.TradeAndUserBo;
import com.meiye.bo.salary.UserAndTradeItm;
import com.meiye.exception.BusinessException;
import com.meiye.model.role.AuthUser;
import com.meiye.model.talent.TalentPlan;
import com.meiye.model.talent.TalentRole;
import com.meiye.model.talent.TalentRule;
import com.meiye.model.trade.Trade;
import com.meiye.model.trade.TradeItem;
import com.meiye.repository.role.AuthUserRepository;
import com.meiye.repository.talent.TalentPlanRepository;
import com.meiye.repository.talent.TalentRoleRepository;
import com.meiye.repository.talent.TalentRuleRepository;
import com.meiye.repository.trade.TradeItemRepository;
import com.meiye.repository.trade.TradeRepository;
import com.meiye.repository.trade.TradeUserRepository;
import com.meiye.service.Salary.SalaryService;
import com.meiye.service.role.AuthRoleService;
import com.meiye.service.role.AuthUserService;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    TalentPlanRepository talentPlanRepository;
    @Autowired
    TalentRuleRepository talentRuleRepository;
    @Autowired
    TalentRoleRepository talentRoleRepository;


    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeItemRepository tradeItemRepository;

    @Override
    public List<SalaryBo> getAllSalary(SalaryBo salaryBo) {
        //得到订单相关的信息
        List<TradeAndUserBo> allSalaryTrade = tradeRepository.getAllSalaryTrade(salaryBo.getStarDate(), salaryBo.getEndDate(), salaryBo.getShopIdenty(), salaryBo.getBrandIdenty());

        //获得销售金额
        if (allSalaryTrade == null || allSalaryTrade.size()==0){
            throw new BusinessException("未查询到用户!");
        }
        Set<Long> userIdSet = new HashSet<>();
        allSalaryTrade.forEach(one ->{
            userIdSet.add(one.getUserId());
        });

        Map<Long,BigDecimal> userSalesBusinessType1 = new HashMap<>();
        Map<Long, BigDecimal> userSalesBusinessType2Or3 = new HashMap<>();
        Map<Integer,List<Long>> roleAndUserList = new HashMap<>();
        Map<Long,List<UserAndTradeItm>> userIdTradeItems = new HashMap<>();
        Map<Long,SalaryBo> userIdSalaryBoMap = new HashMap<>();

        List<UserAndTradeItm> tradeItems = tradeItemRepository.getUserItem(salaryBo.getStarDate(), salaryBo.getEndDate(), salaryBo.getShopIdenty(), salaryBo.getBrandIdenty());
        userIdSet.forEach(userid ->{
            List<UserAndTradeItm> UserAndTradeItm = tradeItems.stream().filter(tradeItem -> tradeItem.getUserId() == userid).collect(Collectors.toList());
            userIdTradeItems.put(userid,UserAndTradeItm);
        });

        for (int i = 0; i < allSalaryTrade.size(); i++) {
            TradeAndUserBo tradeAndUserBo = allSalaryTrade.get(i);
            SalaryBo salary = new SalaryBo(salaryBo.getBrandIdenty(),salaryBo.getShopIdenty(),
                    tradeAndUserBo.getUserId(),tradeAndUserBo.getUserName(),salaryBo.getStarDate(),salaryBo.getEndDate(),new BigDecimal(tradeAndUserBo.getSalaryBase()));
            userIdSalaryBoMap.put(tradeAndUserBo.getUserId(),salary);
            if(tradeAndUserBo.getTradeId() == null){
                continue;
            }
            if (tradeAndUserBo.getBusinessType() == 1){
                getSalesSum(tradeAndUserBo,userSalesBusinessType1);
            }else if (tradeAndUserBo.getBusinessType() == 2 || tradeAndUserBo.getBusinessType() == 3){
                getSalesSum(tradeAndUserBo,userSalesBusinessType2Or3);
            }
            if (roleAndUserList.get(tradeAndUserBo.getRoleId()) != null){
                roleAndUserList.get(tradeAndUserBo.getRoleId()).add(tradeAndUserBo.getUserId());
            }else {
                List<Long> userIds = new ArrayList<>();
                userIds.add(tradeAndUserBo.getUserId());
                roleAndUserList.put(tradeAndUserBo.getRoleId(),userIds);
            }
        }

        //获得提成方案
        List<TalentPlan> talentPlans = talentPlanRepository.findAllByStatusFlagAndEnabledFlagAndBrandIdentyAndShopIdenty(1, 1, salaryBo.getBrandIdenty(), salaryBo.getShopIdenty());
        if (talentPlans !=null && talentPlans.size()>0){
            for (int i = 0; i <talentPlans.size() ; i++) {
                TalentPlan talentPlan = talentPlans.get(i);
                Long talentPlanId = talentPlan.getId();
                List<TalentRole> talentRoles = talentRoleRepository.getTalentRolesByPlanIdAndStatusFlag(talentPlanId, 1);
                List<TalentRule> talentRules = talentRuleRepository.getTalentRuleByPlanIdAndStatusFlagAndOrderByRuleValue(talentPlanId, 1);
                for (int k = 0; k <talentRoles.size() ; k++) {
                    TalentRole talentRole = talentRoles.get(k);
                    Long roleId = talentRole.getRoleId();
                    List<Long> userIds = roleAndUserList.get(roleId);
                    if (userIds != null && userIds.size()>0) {
                        for (int m = 0; m <userIds.size() ; m++) {
                            Long userId = userIds.get(i);
                            BigDecimal saveAmount = userSalesBusinessType2Or3.get(userId);
                            BigDecimal salesAmount = userSalesBusinessType1.get(userId);
                            BigDecimal salesCommissions = new BigDecimal(0);
                            BigDecimal saveCommissions = new BigDecimal(0);
                            if (talentPlan.getPlanType() == 1) {
                                setSalesCommisson(userIdSalaryBoMap, talentPlan, talentRules, userId, salesAmount, salesCommissions, true);
                            } else if (talentPlan.getPlanType() == 2) {
                                setSalesCommisson(userIdSalaryBoMap, talentPlan, talentRules, userId, saveAmount, saveCommissions, false);
                            } else if (talentPlan.getPlanType() == 3) {
                                List<UserAndTradeItm> userAndTradeItms = userIdTradeItems.get(userId);
                                for (int j = 0; j < talentRules.size(); j++) {
                                    TalentRule talentRule = talentRules.get(j);
                                    String dishShopId = talentRule.getDishShopId();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    List<UserAndTradeItm> collect = userAndTradeItms.stream().filter(item -> item.getDishId().equals(dishShopId)).collect(Collectors.toList());
                                    if (collect != null && collect.size() > 0) {
                                        ProjectCommionsDetailBo detail = new ProjectCommionsDetailBo();
                                        detail.setUserId(userId);
                                        detail.setDishId(dishShopId);
                                        detail.setCountAll(collect.size());
                                        detail.setCommissions(new BigDecimal(ruleCommission).multiply(new BigDecimal(collect.size())));
                                        userIdSalaryBoMap.get(userId).getProjectCommionsDetailBos().add(detail);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<SalaryBo>  a = new ArrayList<>(userIdSalaryBoMap.values());
        return a;
    }

    private void setSalesCommisson(Map<Long, SalaryBo> userIdSalaryBoMap, TalentPlan talentPlan, List<TalentRule> talentRules, Long userId, BigDecimal salesAmount, BigDecimal salesCommissions,boolean isSales) {
        StringBuffer saveStr = new StringBuffer();
        if (talentPlan.getPlanMode() ==1) {
            for (int i = talentRules.size(); i >= 0; i--) {
                TalentRule talentRule = talentRules.get(i);
                String ruleValue = talentRule.getRuleValue();
                String ruleCommission = talentRule.getRuleCommission();
                String ruleValuePre;
                if (i == 0){
                    ruleValuePre = "0";
                }else {
                    ruleValuePre = talentRules.get(i-1).getRuleValue();
                }
                if (salesAmount.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                &&  salesAmount.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) >= 0
                ) {
                    //(salesAmount- Double.valueOf(ruleValue))*ruleCommission
                    salesCommissions = salesCommissions.add(salesAmount.
                            subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                    saveStr.append(salesAmount.
                            subtract(new BigDecimal(ruleValue)).toString()
                            +"*" +
                            new BigDecimal(ruleCommission).toString()+
                            "="+ salesAmount.
                            subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)).toString() +";") ;
                    salesAmount = new BigDecimal(ruleValuePre);
                }

            }
        }else if (talentPlan.getPlanMode() ==2){
            for (int i = talentRules.size(); i >= 0; i--) {
                TalentRule talentRule = talentRules.get(i);
                String ruleValue = talentRule.getRuleValue();
                String ruleCommission = talentRule.getRuleCommission();
                String ruleValuePre;
                if (i == 0){
                    ruleValuePre = "0";
                }else {
                    ruleValuePre = talentRules.get(i-1).getRuleValue();
                }
                if (salesAmount.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                        &&  salesAmount.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) >= 0
                ) {
                    //(salesAmount- Double.valueOf(ruleValue))*ruleCommission
                    salesCommissions = salesCommissions.add(new BigDecimal(ruleCommission));
                    saveStr.append(salesAmount.
                            subtract(new BigDecimal(ruleValue)).toString()+

                            " = "+ new BigDecimal(ruleCommission) +";") ;

                    salesAmount = new BigDecimal(ruleValuePre);

                }

            }
        }
        if (isSales){
            userIdSalaryBoMap.get(userId).setSalesCommissions(salesCommissions);
            userIdSalaryBoMap.get(userId).setSalesCommissionsDetail(saveStr.toString());
        }else {
            userIdSalaryBoMap.get(userId).setSaveCommissions(salesCommissions);
            userIdSalaryBoMap.get(userId).setSaveCommissionsDetail(saveStr.toString());
        }

    }

    private void getSalesSum(TradeAndUserBo tradeAndUserBo,Map<Long,BigDecimal> userSalesBusinessType ){
        BigDecimal salesActive = new BigDecimal(0);
        BigDecimal salesInActive = new BigDecimal(0);
        if (tradeAndUserBo.getTradeType() == 1 && tradeAndUserBo.getTradeStatus() ==4 && tradeAndUserBo.getTradePayStatus() ==3){
            salesActive = salesActive.add(new BigDecimal(tradeAndUserBo.getSaleAmount().toString()));
        }else if (tradeAndUserBo.getTradeType() == 2 && tradeAndUserBo.getTradeStatus() ==5 && tradeAndUserBo.getTradePayStatus() ==5){
            salesInActive = salesInActive.add(new BigDecimal(tradeAndUserBo.getSaleAmount().toString()));
        }
        if (userSalesBusinessType.get(tradeAndUserBo.getUserId()) == null){
            userSalesBusinessType.put(tradeAndUserBo.getUserId(),salesActive.subtract(salesInActive));
        }else {
            userSalesBusinessType.put(tradeAndUserBo.getUserId(),userSalesBusinessType.get(tradeAndUserBo.getUserId()).add(salesActive.subtract(salesInActive)));
        }
    }
}
