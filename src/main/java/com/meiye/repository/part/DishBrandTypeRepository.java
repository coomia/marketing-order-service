package com.meiye.repository.part;

import com.meiye.model.part.DishBrandType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 19shusheng.com
 * @Description:
 * @Date: Created in 18:30 2018/8/12
 * @Modified By:
 */
@Repository
public interface DishBrandTypeRepository  extends JpaRepository<DishBrandType,Long> {

    /**
     * 获取所有启用的分类
     * @param statusFlag
     * @return
     */
    List<DishBrandType> findAllByStatusFlagAndBrandIdentyAndShopIdentyOrderBySortDesc(Integer statusFlag,Long brandIdenty,Long shopIdenty);

    @Modifying
    @Query("update DishBrandType bt set bt.name=?1,bt.typeCode=?2 where bt.id=?3")
    int updateDishBrandType(String name,String tyoeCode,Long id);

    @Modifying
    @Query("update DishBrandType bt set bt.statusFlag=?1 where bt.id=?2")
    int deleteDishBrandType(Integer statusFlag,Long id);

}
