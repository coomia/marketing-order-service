package com.meiye.service.part.impl;

import com.meiye.bo.part.DishShopBo;
import com.meiye.model.part.DishSetmeal;
import com.meiye.model.part.DishSetmealGroup;
import com.meiye.model.part.DishShop;
import com.meiye.repository.part.DishSetmealGroupRepository;
import com.meiye.repository.part.DishSetmealRepository;
import com.meiye.repository.part.DishShopRepository;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:03 2018/8/12
 * @Modified By:
 */
@Service
public class DishShopServiceImpl implements DishShopService{

    @Autowired
    DishShopRepository dishShopRepository;

    @Autowired
    DishSetmealRepository dishSetmealRepository;

    @Autowired
    DishSetmealGroupRepository dishSetmealGroupRepository;

    @Override
    public Page<DishShopBo> getDishShopByCriteria(Integer page, Integer size, DishShopBo dishShopBo) {
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"id");

        return null;
    }


    public DishShopBo getDishShopById(Long id){
        DishShop dishShop= dishShopRepository.getOne(id);
        DishShopBo dishShopBo=dishShop.copyTo(DishShopBo.class);
        return dishShopBo;
    }

    public void saveDishShop(DishShopBo dishShopBo){
        if(dishShopBo!=null){
            DishShop dishShop=dishShopBo.copyTo(DishShop.class);
            dishShopRepository.save(dishShop);
            //如果是单品
            if(dishShopBo.getType()==0){

            }
            //如果是套餐
            else if(dishShopBo.getType()==1&&dishShopBo.getDishSetmealGroupBos()!=null){
                dishShopBo.getDishSetmealGroupBos().forEach(dishSetmealGroupBo -> {
                    DishSetmealGroup dishSetmealGroup=dishSetmealGroupBo.copyTo(DishSetmealGroup.class);
                    dishSetmealGroup.setSetmealDishId(dishShop.getId());
                    dishSetmealGroupRepository.save(dishSetmealGroup);
                    if(dishSetmealGroupBo.getDishSetmealBos()!=null){
                        dishSetmealGroupBo.getDishSetmealBos().forEach(dishSetmealBo -> {
                            DishSetmeal dishSetmeal=dishSetmealBo.copyTo(DishSetmeal.class);
                            dishSetmeal.setDishId(dishShop.getId());
                            dishSetmeal.setComboDishTypeId(dishSetmealGroup.getId());
                            dishSetmealRepository.save(dishSetmeal);
                        });
                    }
                });
            }
        }
    }
}
