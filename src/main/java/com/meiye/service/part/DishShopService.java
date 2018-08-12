package com.meiye.service.part;

import com.meiye.bo.part.DishShopBo;
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
     * @param page
     * @param size
     * @param dishShopBo
     * @return
     */
    Page<DishShopBo> getDishShopByCriteria(Integer page, Integer size, DishShopBo dishShopBo);

}
