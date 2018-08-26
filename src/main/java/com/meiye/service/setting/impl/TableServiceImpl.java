package com.meiye.service.setting.impl;

import com.meiye.bo.setting.TableAreaBo;
import com.meiye.bo.setting.TablesBo;
import com.meiye.model.setting.TableArea;
import com.meiye.model.setting.Tables;
import com.meiye.repository.setting.TablesRepository;
import com.meiye.service.setting.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Tables> tablesList = tablesRepository.findAllByStatusFlagAndAreaIdOrderByTableNumAsc(1, areaId);
        if(tablesList!= null && tablesList.size()>0){
            return tablesList.size();
        }
        return 0;
    }

    @Override
    public List<TablesBo> getAllTablesByAreaId(Long areaId) {
        List<Tables> tablesList = tablesRepository.findAllByStatusFlagAndAreaIdOrderByTableNumAsc(1, areaId);
        List<TablesBo> tablesBos = this.copy(tablesList);
        return tablesBos;
    }

    @Override
    public TablesBo updateTable(TablesBo tableBo) {
        tablesRepository.updateTable(tableBo.getTableName(),tableBo.getId());
        return tableBo;
    }

    @Override
    public TablesBo addTable(TablesBo tableBo) {
        Tables tables = tableBo.copyTo(Tables.class);
        tablesRepository.save(tables);
        return tableBo;
    }

    @Override
    public void deleteTable(Long id) {
        tablesRepository.deleteTable(2,id);
    }

    private List<TablesBo> copy(List<Tables> tables){
        if(tables!=null&&!tables.isEmpty()) {
            List<TablesBo> tableBos = new ArrayList<TablesBo>();
            tables.stream().filter(Objects::nonNull).forEach(table->{
                        if(Objects.nonNull(table)){
                            tableBos.add(table.copyTo(TablesBo.class));
                        }
                    }
            );
            return tableBos;
        }else {
            return null;
        }
    }

}
