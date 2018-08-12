package com.meiye.repository.config;

import com.meiye.model.part.DishShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:07 2018/8/12
 * @Modified By:
 */
@Repository
public interface DishShopRepository extends JpaRepository<DishShop,Long>,JpaSpecificationExecutor<DishShop> {


}
