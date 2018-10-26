package com.meiye.service.Salary.impl;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.salary.SalaryBo;
import com.meiye.bo.salary.TradeAndUserBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.role.AuthUser;
import com.meiye.model.talent.TalentPlan;
import com.meiye.model.trade.Trade;
import com.meiye.repository.role.AuthUserRepository;
import com.meiye.repository.talent.TalentPlanRepository;
import com.meiye.repository.talent.TalentRoleRepository;
import com.meiye.repository.talent.TalentRuleRepository;
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
import java.sql.Timestamp;
import java.util.*;


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

    @Override
    public List<SalaryBo> getAllSalary(SalaryBo salaryBo) {
        //得到订单相关的信息
        List<TradeAndUserBo> allSalaryTrade = tradeRepository.getAllSalaryTrade(salaryBo.getStarDate(), salaryBo.getEndDate(), salaryBo.getShopIdenty(), salaryBo.getBrandIdenty());
        //获得销售金额
        if (allSalaryTrade == null || allSalaryTrade.size()==0){
            throw new BusinessException("未查询到该时间段，订单!");
        }

        Map<Long,Double> userSales = new HashMap<>();
        for (int i = 0; i < allSalaryTrade.size(); i++) {
            TradeAndUserBo tradeAndUserBo = allSalaryTrade.get(i);

            Double salesActive = 0D;
            Double salesInActive = 0D;
            if (tradeAndUserBo.getBusinessType() == 1 || tradeAndUserBo.getBusinessType() ==2 || tradeAndUserBo.getBusinessType() ==3){
                if (tradeAndUserBo.getTradeType() == 1 && tradeAndUserBo.getTradeStatus() ==4 && tradeAndUserBo.getTradePayStatus() ==3){
                    salesActive = salesActive + tradeAndUserBo.getSaleAmount();
                }else if (tradeAndUserBo.getTradeType() == 2 && tradeAndUserBo.getTradeStatus() ==5 && tradeAndUserBo.getTradePayStatus() ==5){
                    salesInActive = salesInActive + tradeAndUserBo.getSaleAmount();
                }
            }
            if (userSales.get(tradeAndUserBo.getUserId()) == null){
                userSales.put(tradeAndUserBo.getUserId(),salesActive-salesInActive);
            }else {
                userSales.put(tradeAndUserBo.getUserId(),userSales.get(tradeAndUserBo.getUserId()) +salesActive-salesInActive);
            }
        }


        //获得提成方案
        List<TalentPlan> talentPlans = talentPlanRepository.findAllByStatusFlagAndEnabledFlagAndBrandIdentyAndShopIdenty(1, 1, salaryBo.getBrandIdenty(), salaryBo.getShopIdenty());


        return null;
    }
}
