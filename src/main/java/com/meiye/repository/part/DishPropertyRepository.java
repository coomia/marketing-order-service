package com.meiye.repository.part;

import com.meiye.model.part.DishProperty;
import com.meiye.model.part.DishSetmealGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shawns on 08/13/18.
 */
@Repository
public interface DishPropertyRepository extends JpaRepository<DishProperty,Long> {

    List<DishProperty> findByDishShopIdAndEnabledFlag(Long dishShopId,Long enAbledFlag); //通过ShopId 得到所有的加项

    List<DishProperty> findByIdInAndEnabledFlag(List<Long> id,Long enAbledFlag);//多个id 获取DishProperty

    DishProperty findByIdAndEnabledFlag(Long id,Long enAbledFlag); //通过ID获取DishProperty

}
