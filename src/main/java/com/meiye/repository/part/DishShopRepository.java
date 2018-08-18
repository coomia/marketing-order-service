package com.meiye.repository.part;

import com.meiye.model.part.DishShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Modifying
    @Query("update DishShop ds set ds.name=?1,ds.dishCode=?2,ds.marketPrice=?3,ds.unitName=?4,ds.dishQty=?5 where ds.id=?6")
    public int updateDishShop(String name,String dishCode,Double marketPrice,String unitName,Double dishQty,Long id);


    @Modifying
    @Query("update DishShop ds set ds.statusFlag=2 where ds.id=?1")
    public int deleteDishShop(Long id);
}
