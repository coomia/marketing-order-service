package com.meiye.service.part;

import com.meiye.bo.part.DishPropertyBo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishPropertyService {

    DishPropertyBo save(DishPropertyBo dishPropertyBo); //保存

    DishPropertyBo getOneById(Long id); //单个id 查询

    List<DishPropertyBo> getByIds(List<Long> ids);//批量查询

    void deleteByIds(List<Long> ids); //批量删除

    void deleteById(Long id); //单个删除

    void deleteBySopId(Long dishShopId); //通过shop id 删除
}
