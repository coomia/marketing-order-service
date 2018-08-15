package com.meiye.service.part;

import com.meiye.bo.part.DishBrandTypeBo;

import java.util.List;
import java.util.Map;

/**
 * @Author: ryner
 * @Description :part Category service
 * @Date: Created in 11:58 2018/8/12
 * @Modified By:
 */
public interface BrandTypeService {

    /**
     * 物理删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 获取所有启用的分类列表，并且按照一级分类组装好对应二级分类
     * @return
     */
    List<DishBrandTypeBo> getAllDishBrandTypeList();

    /**
     * 保存或更新实体， 更新前提要求传入参数为完整的实体,不然用update()方法
     * @param dishBrandTypeBo
     * @return
     */
    DishBrandTypeBo saveOrUpdate(DishBrandTypeBo dishBrandTypeBo);

    /**
     * 传入部分字段按照ID更新
     * @param dishBrandTypeBo
     * @return
     */
    int update(DishBrandTypeBo dishBrandTypeBo);

    /**
     * 按照ID获取分类
     * @param id
     * @return
     */
    DishBrandTypeBo getDishBrandType(Long id);

}
