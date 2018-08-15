package com.meiye.service.part;

import com.meiye.bo.part.DishPropertyBo;

import java.util.List;

public interface DishPropertyService {

    void save(DishPropertyBo dishPropertyBo); //保存

    DishPropertyBo getOneById(Long id);

    List<DishPropertyBo> getByIds(List<Long> ids);


}
