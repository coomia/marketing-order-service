package com.meiye.repository.part;

import com.meiye.model.part.DishProperty;
import com.meiye.model.part.DishSetmealGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shawns on 08/13/18.
 */
@Repository
public interface DishPropertyRepository extends JpaRepository<DishProperty,Long> {

    List<DishProperty> findByDishShopIdAndStatusFlag(Long dishShopId,Integer statusFlag); //通过ShopId 得到所有的加项

    List<DishProperty> findByIdInAndEnabledFlag(List<Long> id,Long enAbledFlag);//多个id 获取DishProperty

    DishProperty findByIdAndEnabledFlag(Long id,Long enAbledFlag); //通过ID获取DishProperty

    @Modifying
    @Query(value = "update DishProperty dp set dp.enabledFlag = 2 where dp.id in :ids")
    void deleteByIdS(@Param(value = "ids")List<Long> ids);

    @Modifying
    @Query(value = "update DishProperty dp set dp.enabledFlag = 2 where dp.id = ?1")
    void deleteById(Long id);

    @Modifying
    @Query(value = "update DishProperty dp set dp.enabledFlag = 2 where dp.dishShopId = dishShopId")
    void deleteByShopId(@Param(value = "dishShopId")Long dishShopId);

}
