package com.meiye.service.setting;

import com.meiye.bo.setting.TablesBo;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 19:01 2018/8/26
 * @Modified By:
 */
public interface TableService {

    /**
     * 查看区域下面所有启用的工作台数量
     * @param areaId
     * @return
     */
    int getTablesCountByAreaId(Long areaId);

    /**
     * 查看区域下面所有启用的工作台
     * @param areaId
     * @return
     */
    List<TablesBo> getAllTablesByAreaId(Long areaId);

    TablesBo updateTable(TablesBo tableBo);

    TablesBo addTable(TablesBo tableBo);

    void deleteTable(Long id);

}
