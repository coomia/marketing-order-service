package com.meiye.repository.talent;

import com.meiye.model.talent.TalentPlan;
import com.meiye.model.talent.TalentRole;
import com.meiye.model.talent.TalentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalentRoleRepository extends JpaRepository<TalentRole,Long> {

    @Modifying
    @Query(value = "update TalentRole tp set tp.statusFlag = 2 where tp.planId = ?1")
    void deleteByPlanId(Long id);

    List<TalentRole> getTalentRolesByPlanId(Long id);
}
