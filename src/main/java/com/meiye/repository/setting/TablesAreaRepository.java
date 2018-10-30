package com.meiye.repository.setting;

import com.meiye.model.setting.TableArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:13 2018/8/26
 * @Modified By:
 */
@Repository
public interface TablesAreaRepository extends JpaRepository<TableArea,Long> {

    List<TableArea> findAllByStatusFlagAndBrandIdentyAndShopIdentyOrderByServerCreateTimeAsc(Integer statusFlag,Long brandIdenty,Long shopIdenty);

    @Modifying
    @Query("update TableArea ta set ta.areaName=?1 where ta.id=?2")
    int updateTableArea(String areaName,Long id);

    @Modifying
    @Query("update TableArea ta set ta.statusFlag=?1 where ta.id=?2")
    int deleteTableArea(Integer statusFlag,Long id);
}
