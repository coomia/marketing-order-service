package com.meiye.service.setting.impl;

import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.bo.setting.TableAreaBo;
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
        List<TableArea> tableAreaList = tablesAreaRepository.findAllByStatusFlagOrderByServerCreateTimeAsc(1);
        List<TableAreaBo> tableAreaBoList = this.copy(tableAreaList);
        return tableAreaBoList;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public TableAreaBo updateTableArea(TableAreaBo tableAreaBo) {
       tablesAreaRepository.updateTableArea(tableAreaBo.getAreaName(),tableAreaBo.getId());
        return tableAreaBo;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public TableAreaBo addTableArea(TableAreaBo tableAreaBo) {
        TableArea tableArea = tableAreaBo.copyTo(TableArea.class);
        tablesAreaRepository.save(tableArea);
        return tableAreaBo;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void deleteTableArea(Long id) {
        tablesAreaRepository.deleteTableArea(2,id);
    }


    private List<TableAreaBo> copy(List<TableArea> tableAreas){
        if(tableAreas!=null&&!tableAreas.isEmpty()) {
            List<TableAreaBo> tableAreaBos = new ArrayList<TableAreaBo>();
            tableAreas.stream().filter(Objects::nonNull).forEach(tableArea->{
                        if(Objects.nonNull(tableArea)){
                            tableAreaBos.add(tableArea.copyTo(TableAreaBo.class));
                        }
                    }
            );
            return tableAreaBos;
        }else {
            return null;
        }
    }
}
