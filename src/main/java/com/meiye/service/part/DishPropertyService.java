package com.meiye.service.part;

import com.meiye.bo.part.DishPropertyBo;

public interface DishPropertyService {

    void save(DishPropertyBo dishPropertyBo);
    DishPropertyBo getOne(Long id);


}
