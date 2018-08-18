package com.meiye.repository.part;

import com.meiye.model.part.DishSetmealGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/8/12 0012.
 */
public interface DishSetmealGroupRepository extends JpaRepository<DishSetmealGroup,Long> {

    @Modifying
    @Query("update DishSetmealGroup dsg set dsg.setmealDishId=?1,dsg.name=?2,dsg.orderMin=?3,dsg.orderMax=?4,dsg.updatorName=?5,dsg.updatorId=?6,dsg.statusFlag=1 where dsg.id=?7")
    public int updateDishSetmealGroup(Long setmealDishId,String name,Double orderMin,Double orderMax,String updatorName,Long updatorId, Long id);


    @Modifying
    @Query("update DishSetmealGroup dsg set dsg.statusFlag=2 where dsg.id=?1")
    public int deleteDishSetmealGroup(Long id);

    @Modifying
    @Query("update DishSetmealGroup dsg set dsg.statusFlag=2 where dsg.setmealDishId=?1")
    public int deleteDishSetmealGroupBySetmealDishId(Long setmealDishId);


    public List<DishSetmealGroup> findBySetmealDishIdAndStatusFlag(Long setmealDishId,Integer statusFlag);

}
