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
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


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
        DecimalFormat df2 = new DecimalFormat("#0.00");
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
                            StringBuffer detail = new StringBuffer(talentPlan.getPlanName()+"/plan/");
                            for (int j = 0; j < talentRules.size(); j++) {
                                TalentRule talentRule = talentRules.get(j);
                                float ruleValue = Float.valueOf(talentRule.getRuleValue());
                                float ruleCommission = Float.valueOf(talentRule.getRuleCommission());
                                float ruleValueNext = 0;
                                if (j == talentRules.size()-1) {
                                    ruleValueNext = Integer.MAX_VALUE;
                                } else {
                                    ruleValueNext = Float.valueOf(talentRules.get(j + 1).getRuleValue());
                                }

                                BigDecimal salesSum = salary.getSalesSum();
                                float sum = salesSum.floatValue();
                                if (sum - ruleValue > 0 && sum - ruleValueNext <= 0) {
                                    if (talentPlan.getPlanMode() == 2) {
                                        float com = (sum - ruleValue) * ruleCommission/100;
                                        salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(com).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        detail.append(";" + df2.format(new BigDecimal(sum - ruleValue))
                                                + "*" +
                                                df2.format(new BigDecimal(ruleCommission)) +"%"+
                                                "=" + df2.format(new BigDecimal(com)));
                                    } else if (talentPlan.getPlanMode() == 1) {
                                        salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        detail.append(";" + df2.format(new BigDecimal(sum - ruleValue))
                                                + " : "
                                                + df2.format(new BigDecimal(ruleCommission)));
                                    }

                                } else if (sum - ruleValue >= 0 && sum-ruleValueNext >=0) {
                                    if (talentPlan.getPlanMode() == 2) {
                                        float com = (ruleValueNext - ruleValue) * ruleCommission/100;
                                        salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(com)));

                                        detail.append(";" + df2.format(new BigDecimal(ruleValueNext - ruleValue))
                                                + "*" +
                                                df2.format(new BigDecimal(ruleCommission)) +"%"+
                                                "=" + df2.format(com));
                                    } else if (talentPlan.getPlanMode() == 1) {
                                        salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));

                                        detail.append(";" + df2.format(new BigDecimal(ruleValueNext - ruleValue))
                                                + " : "
                                                + df2.format(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                    }
                                }
                            }

                            salary.setSalesCommissionsDetail(salary.getSalesCommissionsDetail()==""?detail.toString()
                                    :salary.getSalesCommissionsDetail()+"/and/"+detail.toString());
                        } else if (talentPlan.getPlanType() == 3) {
                            StringBuffer detail = new StringBuffer(talentPlan.getPlanName()+"/plan/");
                            for (int j = 0; j < talentRules.size(); j++) {
                                TalentRule talentRule = talentRules.get(j);
                                float ruleValue = Float.valueOf(talentRule.getRuleValue());
                                float ruleCommission = Float.valueOf(talentRule.getRuleCommission());
                                float ruleValueNext = 0;
                                if (j == talentRules.size()-1) {
                                    ruleValueNext = Integer.MAX_VALUE;
                                } else {
                                    ruleValueNext = Float.valueOf(talentRules.get(j + 1).getRuleValue());
                                }

                                BigDecimal saveSum = salary.getSaveSum();
                                float sum = saveSum.floatValue();
                                if (sum - ruleValue > 0 && sum - ruleValueNext <= 0) {
                                    if (talentPlan.getPlanMode() == 2) {
                                        float com = (sum - ruleValue) * ruleCommission/100;
                                        salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(com).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        detail.append(";" + df2.format(new BigDecimal(sum - ruleValue))
                                                + "*" +
                                                df2.format(new BigDecimal(ruleCommission)) +"%"+
                                                "=" + df2.format(new BigDecimal(com)));
                                    } else if (talentPlan.getPlanMode() == 1) {
                                        salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        detail.append(";" + df2.format(new BigDecimal(sum - ruleValue))
                                                + " : "
                                                + df2.format(new BigDecimal(ruleCommission)));
                                    }

                                } else if (sum - ruleValue >= 0 && sum-ruleValueNext >=0) {
                                    if (talentPlan.getPlanMode() == 2) {
                                        float com = (ruleValueNext - ruleValue) * ruleCommission/100;
                                        salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(com)));

                                        detail.append(";" + df2.format(new BigDecimal(ruleValueNext - ruleValue))
                                                + "*" +
                                                df2.format(new BigDecimal(ruleCommission)) +
                                                "=" + df2.format(com));
                                    } else if (talentPlan.getPlanMode() == 1) {
                                        salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));

                                        detail.append(";" + df2.format(new BigDecimal(ruleValueNext - ruleValue))
                                                + "*" +
                                                df2.format(new BigDecimal(ruleCommission)) +"%"+
                                                "=" + df2.format(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                    }
                                }
                            }
                            salary.setSaveCommissionsDetail(salary.getSaveCommissionsDetail()==""?detail.toString()
                                    :salary.getSaveCommissionsDetail()+"/and/"+detail.toString());
                        } else if (talentPlan.getPlanType() == 2) {
                            for (int j = 0; j < talentRules.size(); j++) {
                                TalentRule talentRule = talentRules.get(j);
                                //String dishShopId = talentRule.getDishShopId();
                                String ruleCommission = talentRule.getRuleCommission();
                                String ruleValue = talentRule.getRuleValue();
                                if (getOneUserItem != null && getOneUserItem.size()>0) {
                                    List<UserAndTradeItm> collect = getOneUserItem.stream().filter(
                                            item ->item.getDishId()!=null&& item.getDishId().equals(ruleValue))
                                            .collect(Collectors.toList());
                                    if (collect != null && collect.size() > 0) {
                                        ProjectCommionsDetailBo detail = new ProjectCommionsDetailBo();
                                        detail.setUserId(salaryBo.getUserId());
                                        detail.setDishId(collect.get(0).getDishId());
                                        collect.forEach(UserAndTradeItm ->{
                                            if (detail.getCountAll()!= null) {
                                                detail.setCountAll((detail.getCountAll().add(new BigDecimal(UserAndTradeItm.getQty()))));
                                            }else {
                                                detail.setCountAll(new BigDecimal(UserAndTradeItm.getQty()));
                                            }
                                        });
                                       // detail.setCountAll(collect.size());
                                        detail.setDishName(collect.get(0).getDishName());
                                        detail.setCommissions(new BigDecimal(ruleCommission).multiply(new BigDecimal(collect.size())));
                                        salary.getProjectCommionsDetailBos().add(detail);
                                        salary.setProjectCommissions(
                                        salary.getProjectCommissions().add(new BigDecimal(ruleCommission).multiply(detail.getCountAll())));
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        salary.setSalarySum(salary.getSalesCommissions()
                .add(salary.getSaveCommissions())
                .add(salary.getProjectCommissions())
                .add(salary.getBaseSalary()));
        return salary;
    }

    private void setSaleAndSaveSum(List<TradeAndUserBo> oneSalaryTrade, SalaryBo salary) {
        if (oneSalaryTrade == null || oneSalaryTrade.size()== 0){
            return;
        }

        DecimalFormat df2 = new DecimalFormat("#0.00");

        double salesSum = oneSalaryTrade.stream()
                .filter(tradeAndUserBo -> tradeAndUserBo.getTradeType() != null
                        && tradeAndUserBo.getTradeType() == 1
                        && tradeAndUserBo.getTradeStatus() == 4
                        && (tradeAndUserBo.getBusinessType() == 1 || tradeAndUserBo.getBusinessType() == 4))
                .mapToDouble(value -> value.getSaleAmount())
                .sum();

        double salesRet = oneSalaryTrade.stream()
                .filter(tradeAndUserBo -> tradeAndUserBo.getTradeType() != null
                        && tradeAndUserBo.getTradeType() == 2
                        && tradeAndUserBo.getTradeStatus() == 5
                        && (tradeAndUserBo.getBusinessType() == 1 || tradeAndUserBo.getBusinessType() == 4))
                .mapToDouble(value -> value.getSaleAmount())
                .sum();

        double saveSum = oneSalaryTrade.stream()
                .filter(tradeAndUserBo -> tradeAndUserBo.getTradeType() != null
                        && tradeAndUserBo.getTradeType() == 1
                        && tradeAndUserBo.getTradeStatus() == 4
                        && (tradeAndUserBo.getBusinessType() == 2 || tradeAndUserBo.getBusinessType() == 3))
                .mapToDouble(value -> value.getSaleAmount())
                .sum();

        salary.setSalesSum(new BigDecimal(salesSum-salesRet).setScale(2, BigDecimal.ROUND_HALF_UP));
        salary.setSaveSum(new BigDecimal(saveSum).setScale(2,BigDecimal.ROUND_HALF_UP));
    }

    @Override
    public List<SalaryBo> getAllSalary(SalaryBo salaryBo) {
        DecimalFormat df2 = new DecimalFormat("#0.00");
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
                        SalaryBo salary = salaryBos.get(j);
                        if (salary.getRoleId() == talentRole.getRoleId()) {
                            if (talentPlan.getPlanType() == 1) {
                                for (int s = 0; s < talentRules.size(); s++) {
                                    TalentRule talentRule = talentRules.get(s);
                                    float ruleValue = Float.valueOf(talentRule.getRuleValue());
                                    float ruleCommission = Float.valueOf(talentRule.getRuleCommission());
                                    float ruleValueNext = 0;
                                    if (s == talentRules.size()-1) {
                                        ruleValueNext = Integer.MAX_VALUE;
                                    } else {
                                        ruleValueNext = Float.valueOf(talentRules.get(s + 1).getRuleValue());
                                    }

                                    BigDecimal salesSum = salary.getSalesSum();
                                    float sum = salesSum.floatValue();
                                    if (sum - ruleValue > 0 && sum - ruleValueNext <= 0) {
                                        if (talentPlan.getPlanMode() == 2) {
                                            float com = (sum - ruleValue) * ruleCommission/100;
                                            salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(com).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        } else if (talentPlan.getPlanMode() == 1) {
                                            salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        }

                                    } else if (sum - ruleValue >= 0 && sum-ruleValueNext >=0) {
                                        if (talentPlan.getPlanMode() == 2) {
                                            float com = (ruleValueNext - ruleValue) * ruleCommission/100;
                                            salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(com)));
                                        } else if (talentPlan.getPlanMode() == 1) {
                                            salary.setSalesCommissions(salary.getSalesCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        }
                                    }
                                }
                            } else if (talentPlan.getPlanType() == 3) {
                                for (int m = 0; m < talentRules.size(); m++) {
                                    TalentRule talentRule = talentRules.get(m);
                                    float ruleValue = Float.valueOf(talentRule.getRuleValue());
                                    float ruleCommission = Float.valueOf(talentRule.getRuleCommission());
                                    float ruleValueNext = 0;
                                    if (m == talentRules.size()-1) {
                                        ruleValueNext = Integer.MAX_VALUE;
                                    } else {
                                        ruleValueNext = Float.valueOf(talentRules.get(m + 1).getRuleValue());
                                    }

                                    BigDecimal saveSum = salary.getSaveSum();
                                    float sum = saveSum.floatValue();
                                    if (sum - ruleValue > 0 && sum - ruleValueNext <= 0) {
                                        if (talentPlan.getPlanMode() == 2) {
                                            float com = (sum - ruleValue) * ruleCommission/100;
                                            salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(com).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        } else if (talentPlan.getPlanMode() == 1) {
                                            salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        }

                                    } else if (sum - ruleValue >= 0 && sum-ruleValueNext >=0) {
                                        if (talentPlan.getPlanMode() == 2) {
                                            float com = (ruleValueNext - ruleValue) * ruleCommission/100;
                                            salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(com)));
                                        } else if (talentPlan.getPlanMode() == 1) {
                                            salary.setSaveCommissions(salary.getSaveCommissions().add(new BigDecimal(ruleCommission).setScale(2, BigDecimal.ROUND_HALF_UP)));

                                        }
                                    }
                                }
                            } else if (talentPlan.getPlanType() == 2) {
                                for (int u = 0; u < talentRules.size(); u++) {
                                    TalentRule talentRule = talentRules.get(u);
                                    String dishShopId = talentRule.getDishShopId();
                                    String ruleCommission = talentRule.getRuleCommission();
                                    String ruleValue = talentRule.getRuleValue();

                                    List<UserAndTradeItm> userAndTradeItms = userIdTradeItems.get(salary.getUserId());
                                    if (userAndTradeItms!=null && userAndTradeItms.size()>0){
                                        List<UserAndTradeItm> collect = userAndTradeItms
                                                .stream()
                                                .filter(item -> item.getDishId() != null && item.getDishId().equals(ruleValue))
                                                .collect(Collectors.toList());

                                        if (collect != null && collect.size() > 0) {
                                            double count = collect.stream().mapToDouble(UserAndTradeItm::getQty).sum();
                                            salary.setProjectCommissions(
                                            salary.getProjectCommissions().add(new BigDecimal(ruleCommission).multiply(new BigDecimal(count))));

                                        }
                                    }


                                }
                            }
                        }
                    }
                }
            }
        }

        salaryBos.forEach(salaryBo1 -> {
            salaryBo1.setSalarySum(salaryBo1.getSalesCommissions()
                    .add(salaryBo1.getSaveCommissions())
                    .add(salaryBo1.getProjectCommissions())
                    .add(salaryBo1.getBaseSalary())
            );
        });
        return salaryBos;
    }

}
