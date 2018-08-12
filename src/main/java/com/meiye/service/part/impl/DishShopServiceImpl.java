package com.meiye.service.part.impl;

import com.meiye.bo.part.DishShopBo;
import com.meiye.model.part.DishShop;
import com.meiye.repository.config.DishShopRepository;
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
}
