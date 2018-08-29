package com.meiye.service.setting.impl;

import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.bo.setting.TableAreaBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.part.DishBrandType;
import com.meiye.model.setting.TableArea;
import com.meiye.repository.setting.TablesAreaRepository;
import com.meiye.service.setting.TableAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:29 2018/8/26
 * @Modified By:
 */
@Service
public class TableAreaServiceImpl implements TableAreaService {

    @Autowired
    TablesAreaRepository tablesAreaRepository;


    @Override
    public List<TableAreaBo> getAllTableArea() {
        List<TableArea> tableAreaList = null;
        try {
            tableAreaList = tablesAreaRepository.findAllByStatusFlagOrderByServerCreateTimeAsc(1);
        } catch (Exception e) {
            throw new BusinessException("查找工作台区域失败!");
        }
        List<TableAreaBo> tableAreaBoList = this.copy(tableAreaList);
        return tableAreaBoList;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public TableAreaBo updateTableArea(TableAreaBo tableAreaBo) {
        if (Objects.isNull(tableAreaBo) && Objects.isNull(tableAreaBo.getAreaName())) {
            throw new BusinessException("工作台区域名字不能为空!");
        }
        tablesAreaRepository.updateTableArea(tableAreaBo.getAreaName(), tableAreaBo.getId());
        return tableAreaBo;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public TableAreaBo addTableArea(TableAreaBo tableAreaBo) {
        try {
            TableArea tableArea = tableAreaBo.copyTo(TableArea.class);
            tablesAreaRepository.save(tableArea);
        } catch (Exception e) {
            throw new BusinessException("保存工作台区域失败!");
        }
        return tableAreaBo;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void deleteTableArea(Long id) {
        try {
            tablesAreaRepository.deleteTableArea(2, id);
        } catch (Exception e) {
            throw new BusinessException("删除工作台区域失败!");
        }
    }


    private List<TableAreaBo> copy(List<TableArea> tableAreas) {
        if (tableAreas != null && !tableAreas.isEmpty()) {
            List<TableAreaBo> tableAreaBos = new ArrayList<TableAreaBo>();
            tableAreas.stream().filter(Objects::nonNull).forEach(tableArea -> {
                        if (Objects.nonNull(tableArea)) {
                            tableAreaBos.add(tableArea.copyTo(TableAreaBo.class));
                        }
                    }
            );
            return tableAreaBos;
        } else {
            return null;
        }
    }
}
