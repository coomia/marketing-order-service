package com.meiye.repository.part;

import com.meiye.model.part.DishSetmealGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public interface DishSetmealGroupRepository extends JpaRepository<DishSetmealGroup,Long> {

    @Modifying
    @Query("update DishSetmealGroup dsg set dsg.setmealDishId=?1,dsg.name=?2,dsg.orderMin=?3,dsg.orderMax=?4 where dsg.id=?5")
    public int updateDishSetmealGroup(Long setmealDishId,String name,Double orderMin,Double orderMax, Long id);


    @Modifying
    @Query("update DishSetmealGroup dsg set dsg.statusFlag=2 where dsg.id=?1")
    public int deleteDishSetmealGroup(Long id);

}
