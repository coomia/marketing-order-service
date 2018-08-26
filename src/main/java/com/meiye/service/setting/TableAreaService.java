package com.meiye.service.setting;

import com.meiye.bo.setting.TableAreaBo;
import com.meiye.model.setting.TableArea;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:29 2018/8/26
 * @Modified By:
 */
public interface TableAreaService {

    /**
     * 查看所有启用的工作区域
     * @return
     */
    List<TableAreaBo> getAllTableArea();

    TableAreaBo updateTableArea(TableAreaBo tableAreaBo);

    TableAreaBo addTableArea(TableAreaBo tableAreaBo);

    void deleteTableArea(Long id);
}
