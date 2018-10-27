package com.meiye.repository.talent;

import com.meiye.model.talent.TalentRole;
import com.meiye.model.talent.TalentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalentRuleRepository extends JpaRepository<TalentRule,Long> {
    @Modifying
    @Query(value = "update TalentRule tr set tr.statusFlag = 2 where tr.planId = ?1")
    void deleteByPlanId(Long id);

    List<TalentRule> getTalentRuleByPlanIdAndStatusFlag(Long id,Integer statusFlag);


    @Modifying
    @Query(value = "select tr from TalentRule tr where tr.planId = ?1 and tr.statusFlag =?2 order by tr.ruleValue ")
    List<TalentRule> getTalentRuleByPlanIdAndStatusFlagAndOrderByRuleValue(Long id,Integer statusFlag);


}
