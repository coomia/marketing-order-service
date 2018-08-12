package com.meiye.service.part.impl;

import com.meiye.bo.part.DishShopBo;
import com.meiye.repository.config.DishShopRepository;
import com.meiye.service.part.DishShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public Page<DishShopBo> getDishShopByCriteria(Integer page, Integer size, DishShopBo dishShopBo) {
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"id");

        return null;
    }
}
