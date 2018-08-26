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
    @Query(value = "update TalentRule tr set tr.statusFlag = 2 where tr.planId = id")
    void deleteByPlanId(@Param(value = "id")Long id);

    TalentRule getTalentRuleByPlanId(Long id);
}
