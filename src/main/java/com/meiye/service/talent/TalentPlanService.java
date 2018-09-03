package com.meiye.service.talent;

import com.meiye.bo.talent.TalentPlanBo;
import com.meiye.model.talent.TalentPlan;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TalentPlanService{

    void save(TalentPlanBo talentPlanBo);
    void update(TalentPlanBo talentPlanBo);
    void delete(Long id);
    TalentPlanBo getOne(Long id);
    Page<TalentPlan> getTalentPageByCriteria(Integer pageNum, Integer pageSize, TalentPlanBo talentPlanBo);
}
