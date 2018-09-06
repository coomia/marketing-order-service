package com.meiye.service.setting.impl;

import com.meiye.bo.setting.TablesBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.setting.Tables;
import com.meiye.repository.setting.TablesRepository;
import com.meiye.service.setting.TableService;
import com.meiye.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 19:01 2018/8/26
 * @Modified By:
 */
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    TablesRepository tablesRepository;


    @Override
    public int getTablesCountByAreaId(Long areaId) {
        if (Objects.isNull(areaId)) {
            throw new BusinessException("工作台区域ID不能为空!");
        }
        List<Tables> tablesList = tablesRepository.findAllByStatusFlagAndAreaIdOrderByTableNumAsc(Constants.DATA_ENABLE, areaId);
        if (tablesList != null && tablesList.size() > 0) {
            return tablesList.size();
        }
        return 0;
    }

    @Override
    public List<TablesBo> getAllTablesByAreaId(Long areaId) {
        List<Tables> tablesList = tablesRepository.findAllByStatusFlagAndAreaIdOrderByTableNumAsc(Constants.DATA_ENABLE, areaId);
        List<TablesBo> tablesBos = this.copy(tablesList);
        tablesBos= tablesBos == null ? new ArrayList<>() : tablesBos;
        return tablesBos;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public TablesBo updateTable(TablesBo tableBo) {
        tablesRepository.updateTable(tableBo.getTableName(), tableBo.getId());
        return tableBo;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public Tables addTable(TablesBo tableBo) {
        Tables tables = tableBo.copyTo(Tables.class);
        try {
            tables.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
            tables = tablesRepository.save(tables);
        } catch (Exception e) {
            throw new BusinessException("保存工作台失败!");
        }
        return tables;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void deleteTable(Long id) {
        try {
            tablesRepository.deleteTable(Constants.DATA_DISABLE, id);
        } catch (Exception e) {
            throw new BusinessException("删除工作台失败!");
        }
    }

    private List<TablesBo> copy(List<Tables> tables) {
        if (tables != null && !tables.isEmpty()) {
            List<TablesBo> tableBos = new ArrayList<TablesBo>();
            tables.stream().filter(Objects::nonNull).forEach(table -> {
                        if (Objects.nonNull(table)) {
                            tableBos.add(table.copyTo(TablesBo.class));
                        }
                    }
            );
            return tableBos;
        } else {
            return null;
        }
    }

}
