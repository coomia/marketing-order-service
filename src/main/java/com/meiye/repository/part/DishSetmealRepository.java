package com.meiye.repository.part;

import com.meiye.model.part.DishBrandType;
import com.meiye.model.part.DishSetmeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public interface DishSetmealRepository extends JpaRepository<DishSetmeal,Long> {
    @Modifying
    @Query("update DishSetmeal ds set ds.comboDishTypeId=?1,ds.price=?2,ds.leastCellNum=?3,ds.isReplace=?4,ds.isDefault=?5,ds.isMulti=?6 where ds.id=?5")
    public int updateDishSetmealGroup(Long comboDishTypeId,Double price,Double leastCellNum,Long isReplace, Long isDefault,Long isMulti,Long id);


    @Modifying
    @Query("update DishSetmeal dsg set dsg.statusFlag=2 where dsg.id=?1")
    public int deleteDishSetmeal(Long id);

    public List<DishSetmeal> findByDishIdAndComboDishTypeIdAndStatusFlag(Long dishId,Long comboDishTypeId,Integer statusFlag);


}
