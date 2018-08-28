package com.meiye.service.part.impl;

import com.meiye.bo.part.DishPropertyBo;
import com.meiye.bo.part.DishSetmealBo;
import com.meiye.bo.part.DishSetmealGroupBo;
import com.meiye.bo.part.DishShopBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.part.DishProperty;
import com.meiye.model.part.DishSetmeal;
import com.meiye.model.part.DishSetmealGroup;
import com.meiye.model.part.DishShop;
import com.meiye.repository.part.DishPropertyRepository;
import com.meiye.repository.part.DishSetmealGroupRepository;
import com.meiye.repository.part.DishSetmealRepository;
import com.meiye.repository.part.DishShopRepository;
import com.meiye.service.part.DishShopService;
import com.meiye.system.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<DishShop> getDishShopPageByCriteria(Integer pageNum, Integer pageSize, DishShopBo dishShopBo) {
        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "sort");
        Page<DishShop> shopPage = dishShopRepository.findAll(new Specification<DishShop>() {
            @Override
            public Predicate toPredicate(Root<DishShop> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (null != dishShopBo.getName() && !"".equals(dishShopBo.getName())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), dishShopBo.getName()));
                }
                if (null != dishShopBo.getDishCode() && !"".equals(dishShopBo.getDishCode())) {
                    list.add(criteriaBuilder.like(root.get("dishCode").as(String.class), dishShopBo.getDishCode()));
                }
                if (null != dishShopBo.getType()) {
                    list.add(criteriaBuilder.equal(root.get("type").as(Long.class), dishShopBo.getType()));
                }
                if (null != dishShopBo.getDishTypeId()) {
                    list.add(criteriaBuilder.equal(root.get("dishTypeId").as(Long.class), dishShopBo.getDishTypeId()));
                }
                list.add(criteriaBuilder.equal(root.get("statusFlag").as(Long.class), 1));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return shopPage;
    }

    @Override
    public DishShopBo getDishShopById(Long id) {
        DishShop dishShop=dishShopRepository.findByIdAndShopIdenty(id, WebUtil.getCurrentStoreId());
        if(dishShop==null)
            throw new BusinessException("品项不存在!");
        DishShopBo dishShopBo = dishShop.copyTo(DishShopBo.class);
        //根据shopid得到加项
        if (dishShop != null && dishShop.getType() == 0) {
            //enabledflag : 1 启用 0：停用
            List<DishProperty> dishPropertysByShopID = dishPropertyRepository.findByDishShopIdAndStatusFlag(id, 1);
            if (dishPropertysByShopID != null && dishPropertysByShopID.size() > 0) {
                List<DishPropertyBo> dishPropertyBos = new ArrayList<DishPropertyBo>();
                dishPropertysByShopID.forEach(dishProperty -> {
                    DishPropertyBo dishPropertyBo = dishProperty.copyTo(DishPropertyBo.class);
                    dishPropertyBos.add(dishPropertyBo);
                });
                dishShopBo.setDishPropertyBos(dishPropertyBos);
            }
        }else if(dishShop!=null&&dishShop.getType()==1){
            List<DishSetmealGroup> dishSetmealGroups= dishSetmealGroupRepository.findBySetmealDishIdAndStatusFlag(dishShop.getId(),1);
            List<DishSetmealGroupBo> dishSetmealGroupBos=new ArrayList<DishSetmealGroupBo>();
            if(dishSetmealGroups!=null&&!dishSetmealGroups.isEmpty()) {
                dishSetmealGroups.forEach(dishSetmealGroup -> {
                    DishSetmealGroupBo dishSetmealGroupBo = dishSetmealGroup.copyTo(DishSetmealGroupBo.class);
                    List<DishSetmeal> dishSetmeals = dishSetmealRepository.findByDishIdAndComboDishTypeIdAndStatusFlag(dishShop.getId(), dishSetmealGroup.getId(),1);
                    List<DishSetmealBo> dishSetmealBos=new ArrayList<DishSetmealBo>();
                    if(dishSetmeals!=null&&!dishSetmeals.isEmpty()){
                        dishSetmeals.forEach(dishSetmeal -> {
                            DishSetmealBo dishSetmealBo=dishSetmeal.copyTo(DishSetmealBo.class);
                            DishShop subDishShop=dishShopRepository.findById(dishSetmealBo.getChildDishId()).get();
                            dishSetmealBo.setDishShopBo(subDishShop.copyTo(DishShopBo.class));
                            dishSetmealBos.add(dishSetmealBo);
                        });
                    }
                    dishSetmealGroupBo.setDishSetmealBos(dishSetmealBos);
                    dishSetmealGroupBos.add(dishSetmealGroupBo);
                });
            }
            dishShopBo.setDishSetmealGroupBos(dishSetmealGroupBos);
        }
        return dishShopBo;
    }


    @Override
    @Transactional
    public Long saveDishShop(DishShopBo dishShopBo) {
        if (dishShopBo != null) {
            DishShop dishShop = dishShopBo.copyTo(DishShop.class);
            dishShopRepository.save(dishShop);
            //如果是单品
            if (dishShopBo.getType() == 0 && dishShopBo.getDishPropertyBos() != null && dishShopBo.getDishPropertyBos().size() > 0) {
                dishShopBo.getDishPropertyBos().forEach(dishPropertyBo -> {
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
            return dishShop.getId();
        }
        return null;
    }

    @Override
    @Transactional
    public Long updateDishShop(DishShopBo dishShopBo) {
        if (dishShopBo != null) {
            if (dishShopBo.getId() == null)
                return saveDishShop(dishShopBo);
            else {
                DishShop dishShop = dishShopBo.copyTo(DishShop.class);
                dishShopRepository.updateDishShop(dishShop.getName(), dishShop.getDishCode(), dishShop.getMarketPrice(), dishShop.getUnitName(), dishShop.getDishQty(),dishShop.getUpdatorName(),dishShop.getUpdatorId(), dishShop.getId());
                //如果是单品
                if(dishShopBo.getType() == 0){
                    //更新的话先把加项全部删除  然后再把新增和修改的save
                    dishPropertyRepository.deleteByShopId(dishShopBo.getId());
                    if(dishShopBo.getDishPropertyBos() != null && dishShopBo.getDishPropertyBos().size() > 0){
                        dishShopBo.getDishPropertyBos().forEach(dishPropertyBo -> {
                            DishProperty dishProperty = dishPropertyBo.copyTo(DishProperty.class);
                            dishProperty.setDishShopId(dishShop.getId());
                            dishPropertyRepository.save(dishProperty);
                        });
                    }
                } else if (dishShopBo.getType() == 1) {//套餐
                    dishSetmealGroupRepository.deleteDishSetmealGroupBySetmealDishId(dishShop.getId());
                    dishShopBo.getDishSetmealGroupBos().forEach(dishSetmealGroupBo -> {
                        DishSetmealGroup dishSetmealGroup = dishSetmealGroupBo.copyTo(DishSetmealGroup.class);
                        dishSetmealGroup.setSetmealDishId(dishShop.getId());
                        if (dishSetmealGroup.getId() == null)
                            dishSetmealGroupRepository.save(dishSetmealGroup);
                        else
                            dishSetmealGroupRepository.updateDishSetmealGroup(dishSetmealGroup.getSetmealDishId(), dishSetmealGroup.getName(), dishSetmealGroup.getOrderMin(), dishSetmealGroup.getOrderMax(),dishSetmealGroup.getUpdatorName(),dishSetmealGroup.getUpdatorId(), dishSetmealGroup.getId());
                        if (dishSetmealGroupBo.getDishSetmealBos() != null) {
                            dishSetmealRepository.deleteDishSetmealByGroupAndDishId(dishShop.getId(),dishSetmealGroup.getId());
                            dishSetmealGroupBo.getDishSetmealBos().forEach(dishSetmealBo -> {
                                if (dishSetmealBo.getStatusFlag() == 1l) {
                                    DishSetmeal dishSetmeal = dishSetmealBo.copyTo(DishSetmeal.class);
                                    dishSetmeal.setDishId(dishShop.getId());
                                    dishSetmeal.setComboDishTypeId(dishSetmealGroup.getId());
                                    if (dishSetmeal.getId() == null) {
                                        dishSetmealRepository.save(dishSetmeal);
                                    } else {
                                        dishSetmealRepository.updateDishSetmealGroup(dishSetmeal.getComboDishTypeId(), dishSetmeal.getPrice(), dishSetmeal.getLeastCellNum(), dishSetmeal.getIsReplace(), dishSetmeal.getIsDefault(), dishSetmeal.getIsMulti(),dishSetmeal.getUpdatorName(),dishSetmeal.getUpdatorId(), dishSetmeal.getId());
                                    }
                                } else if (dishSetmealBo.getId() != null) {
                                    dishSetmealRepository.deleteDishSetmeal(dishSetmealBo.getId());
                                }
                            });
                        }
                    });
                }
                return dishShop.getId();
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteDishShop(Long shopId) {
        if (shopId != null) {
            dishShopRepository.deleteDishShop(shopId);
        }
    }
}
