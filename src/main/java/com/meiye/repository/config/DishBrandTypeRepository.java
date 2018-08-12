package com.meiye.repository.config;

import com.meiye.model.config.AppConfig;
import com.meiye.model.part.DishBrandType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: 19shusheng.com
 * @Description:
 * @Date: Created in 18:30 2018/8/12
 * @Modified By:
 */
public interface DishBrandTypeRepository  extends JpaRepository<DishBrandType,Long> {

    /**
     * 获取所有启用的分类
     * @param enabledFlag
     * @return
     */
    List<DishBrandType> findAllByEnabledFlagOrderBySortDesc(Long enabledFlag);

    @Modifying
    @Query("update DishBrandType bt set bt.name=?1,bt.typeCode=?2 where bt.id=?3")
    int updateDishBrandType(String name,String tyoeCode,Long id);

    @Modifying
    @Query("update DishBrandType bt set bt.enabledFlag=?1 where bt.id=?2")
    int deleteDishBrandType(Long enabledFlag,Long id);

}
