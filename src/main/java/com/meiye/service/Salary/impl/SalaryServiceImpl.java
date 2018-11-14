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
    public SalaryBo getOneSalary(SalaryBo salaryBo) {
        //得到一个用户的所有订单
        List<TradeAndUserBo> oneSalaryTrade = tradeRepository.getOneSalaryTrade(salaryBo.getStartDate(), salaryBo.getEndDate()
                , salaryBo.getShopIdenty(), salaryBo.getBrandIdenty(), salaryBo.getUserId());

        if (oneSalaryTrade == null || oneSalaryTrade.size() == 0) {
            throw new BusinessException("未查询到用户信息，请核对数据!");
        }

        SalaryBo salary = new SalaryBo();
        salary.setUserId(oneSalaryTrade.get(0).getUserId());
        salary.setUserName(oneSalaryTrade.get(0).getUserName());
        salary.setRoleId(oneSalaryTrade.get(0).getRoleId());
        salary.setStartDate(salaryBo.getStartDate());
        salary.setEndDate(salaryBo.getEndDate());
        salary.setBaseSalary(new BigDecimal(oneSalaryTrade.get(0).getSalaryBase() == null ? "0" : oneSalaryTrade.get(0).getSalaryBase()));

        //得到销售的所有金额  销售额+加储蓄额
        setSaleAndSaveSum(oneSalaryTrade, salary);

        //得到一个用户的所有销售项目
        List<UserAndTradeItm> getOneUserItem = tradeItemRepository.getOneUserItem(salaryBo.getStartDate(), salaryBo.getEndDate()
                , salaryBo.getShopIdenty(), salaryBo.getBrandIdenty(), salaryBo.getUserId());

        //获得提成方案
        List<TalentPlan> talentPlans = talentPlanRepository.findAllByStatusFlagAndEnabledFlagAndBrandIdentyAndShopIdenty(1, 1, salaryBo.getBrandIdenty(), salaryBo.getShopIdenty());
        if (talentPlans != null && talentPlans.size() > 0) {
            for (int i = 0; i < talentPlans.size(); i++) {
                TalentPlan talentPlan = talentPlans.get(i);
                Long talentPlanId = talentPlan.getId();
                List<TalentRole> talentRoles = talentRoleRepository.getTalentRolesByPlanIdAndStatusFlag(talentPlanId, 1);
                List<TalentRule> talentRules = talentRuleRepository.getTalentRuleByPlanIdAndStatusFlagAndOrderByRuleValue(talentPlanId, 1);

                for (int k = 0; k < talentRoles.size(); k++) {
                    TalentRole talentRole = talentRoles.get(k);
                    if (salary.getRoleId().intValue() == talentRole.getRoleId()) {
                        if (talentPlan.getPlanType() == 1) {
                            StringBuffer detail = new StringBuffer(talentPlan.getPlanName());
                            if (talentPlan.getPlanMode() == 1) {
                                for (int j = 0; j < talentRules.size(); j++) {
                                    TalentRule talentRule = talentRules.get(j);
                                    String ruleValue = talentRule.getRuleValue();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    String ruleValuePre = "";
                                    if (j == 0) {
                                        ruleValuePre = "0";
                                    } else {
                                        ruleValuePre = talentRules.get(j - 1).getRuleValue();
                                    }

                                    BigDecimal salesSum = salary.getSalesSum();
                                    if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                            && salesSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(
                                                salesSum.subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                                        detail.append(";" + salesSum.
                                                subtract(new BigDecimal(ruleValue)).toString()
                                                + "*" +
                                                new BigDecimal(ruleCommission).toString() +
                                                "=" + salesSum.subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                                    } else if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(
                                                new BigDecimal(ruleValue).subtract(new BigDecimal(ruleValuePre)).multiply(new BigDecimal(ruleCommission)));
                                        detail.append(";" + new BigDecimal(ruleValue).
                                                subtract(new BigDecimal(ruleValuePre)).toString()
                                                + "*" +
                                                new BigDecimal(ruleCommission).toString() +
                                                "=" +
                                                new BigDecimal(ruleValue).subtract(new BigDecimal(ruleValuePre)).multiply(new BigDecimal(ruleCommission)));
                                    }
                                }
                            } else if (talentPlan.getPlanMode() == 2) {
                                for (int j = 0; j < talentRules.size(); j++) {
                                    TalentRule talentRule = talentRules.get(j);
                                    String ruleValue = talentRule.getRuleValue();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    String ruleValuePre = "";

                                    if (j == 0) {
                                        ruleValuePre = "0";
                                    } else {
                                        ruleValuePre = talentRules.get(j - 1).getRuleValue();
                                    }

                                    BigDecimal salesSum = salary.getSalesSum();
                                    if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                            && salesSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(new BigDecimal(ruleCommission));
                                        detail.append(";" + salesSum.
                                                subtract(new BigDecimal(ruleValue)).toString() +
                                                " = " + new BigDecimal(ruleCommission));

                                    } else if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(new BigDecimal(ruleCommission));
                                        detail.append(";" + new BigDecimal(ruleValue).
                                                subtract(new BigDecimal(ruleValuePre)).toString() +
                                                " = " + new BigDecimal(ruleCommission));
                                    }
                                }
                            }
                            salary.setSalesCommissionsDetail(detail.toString());
                        } else if (talentPlan.getPlanType() == 2) {
                            StringBuffer detail = new StringBuffer(talentPlan.getPlanName());
                            if (talentPlan.getPlanMode() == 1) {
                                for (int j = 0; j < talentRules.size(); j++) {
                                    TalentRule talentRule = talentRules.get(j);
                                    String ruleValue = talentRule.getRuleValue();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    String ruleValuePre = "";
                                    if (j == 0) {
                                        ruleValuePre = "0";
                                    } else {
                                        ruleValuePre = talentRules.get(j - 1).getRuleValue();
                                    }

                                    BigDecimal saveSum = salary.getSaveSum();
                                    if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                            && saveSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(
                                                saveSum.subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                                        detail.append(";" + saveSum.
                                                subtract(new BigDecimal(ruleValue)).toString()
                                                + "*" +
                                                new BigDecimal(ruleCommission).toString() +
                                                "=" + saveSum.subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                                    } else if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(
                                                new BigDecimal(ruleValue).subtract(new BigDecimal(ruleValuePre)).multiply(new BigDecimal(ruleCommission)));
                                        detail.append(";" + new BigDecimal(ruleValue).
                                                subtract(new BigDecimal(ruleValuePre)).toString()
                                                + "*" +
                                                new BigDecimal(ruleCommission).toString() +
                                                "=" +
                                                new BigDecimal(ruleValue).subtract(new BigDecimal(ruleValuePre)).multiply(new BigDecimal(ruleCommission)));
                                    }
                                }
                            } else if (talentPlan.getPlanMode() == 2) {
                                for (int j = 0; j < talentRules.size(); j++) {
                                    TalentRule talentRule = talentRules.get(j);
                                    String ruleValue = talentRule.getRuleValue();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    String ruleValuePre = "";
                                    if (j == 0) {
                                        ruleValuePre = "0";
                                    } else {
                                        ruleValuePre = talentRules.get(j - 1).getRuleValue();
                                    }

                                    BigDecimal saveSum = salary.getSaveSum();
                                    if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                            && saveSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(new BigDecimal(ruleCommission));
                                        detail.append(";" + saveSum.
                                                subtract(new BigDecimal(ruleValue)).toString() +
                                                " = " + new BigDecimal(ruleCommission));

                                    } else if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                        salary.getSalesCommissions().add(new BigDecimal(ruleCommission));
                                        detail.append(";" + new BigDecimal(ruleValue).
                                                subtract(new BigDecimal(ruleValuePre)).toString() +
                                                " = " + new BigDecimal(ruleCommission));
                                    }
                                }
                            }
                            salary.setSaveCommissionsDetail(detail.toString());
                        } else if (talentPlan.getPlanType() == 3) {
                            for (int j = 0; j < talentRules.size(); j++) {
                                TalentRule talentRule = talentRules.get(j);
                                String dishShopId = talentRule.getDishShopId();
                                String ruleCommission = talentRule.getRuleCommission();
                                List<UserAndTradeItm> collect = getOneUserItem.stream().filter(item -> item.getDishId().equals(dishShopId)).collect(Collectors.toList());
                                if (collect != null && collect.size() > 0) {
                                    ProjectCommionsDetailBo detail = new ProjectCommionsDetailBo();
                                    detail.setUserId(salaryBo.getUserId());
                                    detail.setDishId(dishShopId);
                                    detail.setCountAll(collect.size());
                                    detail.setCommissions(new BigDecimal(ruleCommission).multiply(new BigDecimal(collect.size())));
                                    salary.getProjectCommionsDetailBos().add(detail);
                                    salary.getProjectCommissions().add(new BigDecimal(ruleCommission).multiply(new BigDecimal(collect.size())));
                                }
                            }
                        }
                    }

                }
            }
        }


        return salary;
    }

    private void setSaleAndSaveSum(List<TradeAndUserBo> oneSalaryTrade, SalaryBo salary) {
        BigDecimal salesSum = new BigDecimal(0);
        BigDecimal savesSum = new BigDecimal(0);

        for (TradeAndUserBo tradeAndUserBo : oneSalaryTrade) {
            if (tradeAndUserBo.getTradeId() == null || tradeAndUserBo.getBusinessType() == null
                    || tradeAndUserBo.getTradePayStatus() == null || tradeAndUserBo.getTradeStatus() == null) {
                continue;
            }
            if (tradeAndUserBo.getBusinessType() == 1) {
                getSumSales(tradeAndUserBo, salesSum);
            } else if (tradeAndUserBo.getBusinessType() == 2 || tradeAndUserBo.getBusinessType() == 3) {
                getSumSales(tradeAndUserBo, savesSum);
            }
        }
        salary.setSalesSum(salesSum);
        salary.setSaveSum(savesSum);
    }


    private BigDecimal getSumSales(TradeAndUserBo tradeAndUserBo, BigDecimal salesSum) {
        if (tradeAndUserBo.getTradeType() == 1 && tradeAndUserBo.getTradeStatus() == 4 && tradeAndUserBo.getTradePayStatus() == 3) {
            salesSum = salesSum.add(new BigDecimal(tradeAndUserBo.getSaleAmount().toString()));
        } else if (tradeAndUserBo.getTradeType() == 2 && tradeAndUserBo.getTradeStatus() == 5 && tradeAndUserBo.getTradePayStatus() == 5) {
            salesSum = salesSum.subtract(new BigDecimal(tradeAndUserBo.getSaleAmount().toString()));
        }
        return salesSum;
    }

    @Override
    public List<SalaryBo> getAllSalary(SalaryBo salaryBo) {
        //得到订单相关的信息
        List<TradeAndUserBo> allSalaryTrade = tradeRepository.getAllSalaryTrade(salaryBo.getStartDate(), salaryBo.getEndDate(), salaryBo.getShopIdenty(), salaryBo.getBrandIdenty());

        //获得销售金额
        if (allSalaryTrade == null || allSalaryTrade.size() == 0) {
            throw new BusinessException("未查询到用户!");
        }
        List<SalaryBo> salaryBos = new ArrayList<>();

        Map<Long, List<TradeAndUserBo>> userTradeMap = allSalaryTrade.stream()
                .collect(Collectors.groupingBy(o -> o.getUserId()));

        for (Long key : userTradeMap.keySet()) {
            List<TradeAndUserBo> tradeAndUserBos = userTradeMap.get(key);
            SalaryBo salary = new SalaryBo();
            salary.setUserId(tradeAndUserBos.get(0).getUserId());
            salary.setUserName(tradeAndUserBos.get(0).getUserName());
            salary.setRoleId(tradeAndUserBos.get(0).getRoleId());
            salary.setStartDate(salaryBo.getStartDate());
            salary.setEndDate(salaryBo.getEndDate());
            salary.setBaseSalary(new BigDecimal(tradeAndUserBos.get(0).getSalaryBase() == null ? "0" : tradeAndUserBos.get(0).getSalaryBase()));
            setSaleAndSaveSum(tradeAndUserBos, salary);
            setSaleAndSaveSum(tradeAndUserBos, salary);
            salaryBos.add(salary);
        }

        List<UserAndTradeItm> tradeItems = tradeItemRepository.getUserItem(salaryBo.getStartDate(), salaryBo.getEndDate(), salaryBo.getShopIdenty(), salaryBo.getBrandIdenty());
        Map<Long, List<UserAndTradeItm>> userIdTradeItems = tradeItems.stream().collect(Collectors.groupingBy(o -> o.getUserId()));


        //获得提成方案
        List<TalentPlan> talentPlans = talentPlanRepository.findAllByStatusFlagAndEnabledFlagAndBrandIdentyAndShopIdenty(1, 1, salaryBo.getBrandIdenty(), salaryBo.getShopIdenty());
        if (talentPlans != null && talentPlans.size() > 0) {
            for (int i = 0; i < talentPlans.size(); i++) {
                TalentPlan talentPlan = talentPlans.get(i);
                Long talentPlanId = talentPlan.getId();
                List<TalentRole> talentRoles = talentRoleRepository.getTalentRolesByPlanIdAndStatusFlag(talentPlanId, 1);
                List<TalentRule> talentRules = talentRuleRepository.getTalentRuleByPlanIdAndStatusFlagAndOrderByRuleValue(talentPlanId, 1);
                for (int k = 0; k < talentRoles.size(); k++) {
                    TalentRole talentRole = talentRoles.get(k);
                    for (int j = 0; j < salaryBos.size(); j++) {
                        SalaryBo one = salaryBos.get(j);
                        if (one.getRoleId() == talentRole.getRoleId()) {
                            if (talentPlan.getPlanType() == 1) {
                                if (talentPlan.getPlanMode() == 1) {
                                    for (int m = 0; m < talentRules.size(); m++) {
                                        TalentRule talentRule = talentRules.get(m);
                                        String ruleValue = talentRule.getRuleValue();
                                        String ruleCommission = talentRule.getRuleCommission();
                                        String ruleValuePre = "";
                                        if (m == 0) {
                                            ruleValuePre = "0";
                                        } else {
                                            ruleValuePre = talentRules.get(m - 1).getRuleValue();
                                        }

                                        BigDecimal salesSum = one.getSalesSum();
                                        if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                                && salesSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(
                                                    salesSum.subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                                        } else if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(
                                                    new BigDecimal(ruleValue).subtract(new BigDecimal(ruleValuePre)).multiply(new BigDecimal(ruleCommission)));
                                        }
                                    }
                                } else if (talentPlan.getPlanMode() == 2) {
                                    for (int m = 0; m < talentRules.size(); m++) {
                                        TalentRule talentRule = talentRules.get(m);
                                        String ruleValue = talentRule.getRuleValue();
                                        String ruleCommission = talentRule.getRuleCommission();
                                        String ruleValuePre = "";

                                        if (m == 0) {
                                            ruleValuePre = "0";
                                        } else {
                                            ruleValuePre = talentRules.get(m - 1).getRuleValue();
                                        }

                                        BigDecimal salesSum = one.getSalesSum();
                                        if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                                && salesSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(new BigDecimal(ruleCommission));

                                        } else if (salesSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(new BigDecimal(ruleCommission));
                                        }
                                    }
                                }
                            } else if (talentPlan.getPlanType() == 2) {
                                if (talentPlan.getPlanMode() == 1) {
                                    for (int m = 0; m < talentRules.size(); m++) {
                                        TalentRule talentRule = talentRules.get(m);
                                        String ruleValue = talentRule.getRuleValue();
                                        String ruleCommission = talentRule.getRuleCommission();
                                        String ruleValuePre = "";
                                        if (m == 0) {
                                            ruleValuePre = "0";
                                        } else {
                                            ruleValuePre = talentRules.get(m - 1).getRuleValue();
                                        }

                                        BigDecimal saveSum = one.getSaveSum();
                                        if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                                && saveSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(
                                                    saveSum.subtract(new BigDecimal(ruleValue)).multiply(new BigDecimal(ruleCommission)));
                                        } else if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(
                                                    new BigDecimal(ruleValue).subtract(new BigDecimal(ruleValuePre)).multiply(new BigDecimal(ruleCommission)));
                                        }
                                    }
                                } else if (talentPlan.getPlanMode() == 2) {
                                    for (int m = 0; m < talentRules.size(); m++) {
                                        TalentRule talentRule = talentRules.get(m);
                                        String ruleValue = talentRule.getRuleValue();
                                        String ruleCommission = talentRule.getRuleCommission();
                                        String ruleValuePre = "";
                                        if (m == 0) {
                                            ruleValuePre = "0";
                                        } else {
                                            ruleValuePre = talentRules.get(m - 1).getRuleValue();
                                        }

                                        BigDecimal saveSum = one.getSaveSum();
                                        if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) <= 0
                                                && saveSum.subtract(new BigDecimal(ruleValuePre)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(new BigDecimal(ruleCommission));

                                        } else if (saveSum.subtract(new BigDecimal(ruleValue)).compareTo(new BigDecimal(0)) > 0) {
                                            one.getSalesCommissions().add(new BigDecimal(ruleCommission));
                                        }
                                    }
                                }
                            } else if (talentPlan.getPlanType() == 3) {
                                for (int m = 0; m < talentRules.size(); m++) {
                                    TalentRule talentRule = talentRules.get(j);
                                    String dishShopId = talentRule.getDishShopId();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    List<UserAndTradeItm> collect = userIdTradeItems.get(one.getUserId()).stream().filter(item -> item.getDishId().equals(dishShopId)).collect(Collectors.toList());
                                    if (collect != null && collect.size() > 0) {
                                        one.getProjectCommissions().add(new BigDecimal(ruleCommission).multiply(new BigDecimal(collect.size())));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return salaryBos;
    }

}
