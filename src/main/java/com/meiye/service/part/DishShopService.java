package com.meiye.service.part;

import com.meiye.bo.part.DishShopBo;
import com.meiye.model.part.DishShop;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:02 2018/8/12
 * @Modified By:
 */
public interface DishShopService {

    /**
     * 根据查询条件查询商品信息
     * @param pageNum
     * @param pageSize
     * @param dishShopBo
     * @return
     */
    Page<DishShop> getDishShopByCriteria(Integer pageNum, Integer pageSize, DishShopBo dishShopBo);

}