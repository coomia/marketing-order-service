package com.meiye.service.part.impl;

import com.meiye.bo.part.DishPropertyBo;
import com.meiye.bo.part.DishShopBo;
import com.meiye.model.part.DishProperty;
import com.meiye.model.part.DishSetmeal;
import com.meiye.model.part.DishSetmealGroup;
import com.meiye.model.part.DishShop;
import com.meiye.repository.part.DishPropertyRepository;
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

    @Autowired
    DishPropertyRepository dishPropertyRepository;

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
        //根据shopid得到加项
        if(dishShop != null && dishShop.getType()==0){
            //enabledflag : 1 启用 0：停用
            List<DishProperty> dishPropertysByShopID = dishPropertyRepository.findByDishShopIdAndEnabledFlag(id,1L);
            if(dishPropertysByShopID != null && dishPropertysByShopID.size()>0){
                List<DishPropertyBo> dishPropertyBos = new ArrayList<DishPropertyBo>();
                dishPropertysByShopID.forEach(dishProperty ->{
                    DishPropertyBo dishPropertyBo = dishProperty.copyTo(DishPropertyBo.class);
                    dishPropertyBos.add(dishPropertyBo);
                });
                dishShopBo.setDishPropertyBos(dishPropertyBos);
            }
        }
        return dishShopBo;
    }

    public void saveDishShop(DishShopBo dishShopBo){
        if(dishShopBo!=null){
            DishShop dishShop=dishShopBo.copyTo(DishShop.class);
            dishShopRepository.save(dishShop);
            //如果是单品
            if(dishShopBo.getType()==0 && dishShopBo.getDishPropertyBos() != null && dishShopBo.getDishPropertyBos().size()>0){
                dishShopBo.getDishPropertyBos().forEach(dishPropertyBo ->{
                    DishProperty dishProperty = dishPropertyBo.copyTo(DishProperty.class);
                    dishProperty.setDishShopId(dishShop.getId());
                    dishPropertyRepository.save(dishProperty);
                });
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

    public void updateDishShop(DishShopBo dishShopBo){
        if(dishShopBo != null){
            //如果是单品
            if(dishShopBo.getType()==0){

            }else if(true){//套餐

            }
        }
    }

    public void deleteDishShop(DishShopBo dishShopBo){
        if(dishShopBo != null){
            //如果是单品
            if(dishShopBo.getType()==0){

            }else if(true){//套餐

            }
        }
    }
}
