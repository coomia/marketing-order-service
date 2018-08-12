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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
    public Page<DishShop> getDishShopByCriteria(Integer pageNum, Integer pageSize, DishShopBo dishShopBo) {
        Pageable pageable = new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"sort");
        Page<DishShop> shopPage = dishShopRepository.findAll(new Specification<DishShop>(){
            @Override
            public Predicate toPredicate(Root<DishShop> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(null!=dishShopBo.getName()&&!"".equals(dishShopBo.getName())){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), dishShopBo.getName()));
                }
                if(null!= dishShopBo.getDishCode()&&!"".equals( dishShopBo.getDishCode())){
                    list.add(criteriaBuilder.like(root.get("dishCode").as(String.class), dishShopBo.getDishCode()));
                }
                if(null!=dishShopBo.getType()){
                    list.add(criteriaBuilder.equal(root.get("type").as(Long.class), dishShopBo.getType()));
                }
                if(null!=dishShopBo.getDishTypeId()){
                    list.add(criteriaBuilder.equal(root.get("dishTypeId").as(Long.class), dishShopBo.getDishTypeId()));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        },pageable);
        return shopPage;
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
