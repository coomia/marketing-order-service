package com.meiye.service.part.impl;

import com.meiye.bo.part.DishPropertyBo;
import com.meiye.model.part.DishProperty;
import com.meiye.repository.part.DishPropertyRepository;
import com.meiye.service.part.DishPropertyService;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishPropertyServiceImpl implements DishPropertyService{

    @Autowired
    DishPropertyRepository dishPropertyRepository;

    @Override
    public void save(DishPropertyBo dishPropertyBo) {
        if(dishPropertyBo != null){
            DishProperty dishProperty = dishPropertyBo.copyTo(DishProperty.class);
            dishPropertyRepository.save(dishProperty);
        }
    }

    @Override
    public DishPropertyBo getOneById(Long id) {
        DishProperty dishProperty = dishPropertyRepository.findByIdAndEnabledFlag(id, 1L);
        if(dishProperty != null){
            DishPropertyBo dishPropertyBo = dishProperty.copyTo(DishPropertyBo.class);
            return dishPropertyBo;
        }
        return null;
    }

    @Override
    public List<DishPropertyBo> getByIds(List<Long> ids) {
        List<DishProperty> dishPropertys = dishPropertyRepository.findByIdInAndEnabledFlag(ids, 1L);
        if(dishPropertys != null && dishPropertys.size()>0){
            List<DishPropertyBo> dishPropertyBos = new ArrayList<>();
            for (DishProperty dishProperty : dishPropertys) {
                dishPropertyBos.add(dishProperty.copyTo(DishPropertyBo.class));
            }
            return dishPropertyBos;
        }
        return null;
    }
}
