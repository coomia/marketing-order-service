package com.meiye.service.talent;

import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.model.talent.TalentPlan;

public interface TalentPlanService{

    void save(TalentPlanBo talentPlanBo);
    void update(TalentPlanBo talentPlanBo);
    void delete(Long id);
    TalentPlanBo getOne(Long id);
}
