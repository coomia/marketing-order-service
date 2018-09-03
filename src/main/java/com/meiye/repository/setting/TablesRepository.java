package com.meiye.repository.setting;

import com.meiye.model.setting.Tables;
import com.meiye.model.trade.CustomerCardTime;
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
public interface TablesRepository extends JpaRepository<Tables,Long> {

    /**
     * 根据区域ID查看所有启用的工作台，并按照台号升序排
     * @param statusFlag
     * @param areaId
     * @return
     */
    List<Tables> findAllByStatusFlagAndAreaIdOrderByTableNumAsc(Integer statusFlag,Long areaId);

    List<Tables> findAllByStatusFlag(Integer statusFlag);

    @Modifying
    @Query("update Tables ta set ta.tableName=?1 where ta.id=?2")
    int updateTable(String tableName,Long id);

    @Modifying
    @Query("update Tables ta set ta.statusFlag=?1 where ta.id=?2")
    int deleteTable(Integer statusFlag,Long id);
}
