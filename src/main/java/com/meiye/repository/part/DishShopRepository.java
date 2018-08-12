package com.meiye.repository.part;

import com.meiye.model.part.DishShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:07 2018/8/12
 * @Modified By:
 */
public interface DishShopRepository extends JpaRepository<DishShop,Long>,JpaSpecificationExecutor<DishShop> {
}
