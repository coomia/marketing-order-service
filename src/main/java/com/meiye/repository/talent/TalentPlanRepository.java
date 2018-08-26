package com.meiye.repository.talent;

import com.meiye.model.store.Commercial;
import com.meiye.model.talent.TalentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TalentPlanRepository extends JpaRepository<TalentPlan,Long> {
    @Modifying
    @Query(value = "update TalentPlan tp set tp.statusFlag = 2 where tp.id = id")
    void delete(@Param(value = "id")Long id);

}
