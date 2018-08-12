package com.meiye.service.part.impl;

import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.part.DishBrandPropertyBo;
import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.model.config.AppConfig;
import com.meiye.model.part.DishBrandType;
import com.meiye.repository.config.DishBrandTypeRepository;
import com.meiye.service.part.BrandTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @Author: ryner
 * @Description: part Category service
 * @Date: Created in 11:58 2018/8/12
 * @Modified By:
 */
@Service
public class BrandTypeServiceImpl implements BrandTypeService {

    @Autowired
    DishBrandTypeRepository dishBrandTypeRepository;

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public int delete(@NotNull Long id) {
       return dishBrandTypeRepository.deleteDishBrandType(new Long(2),id);
    }

    @Override
    public Map<DishBrandTypeBo,List<DishBrandTypeBo>> getAllDishBrandTypeList() {
        List<DishBrandType> dishBrandTypes = dishBrandTypeRepository.findAllByEnabledFlagOrderBySortDesc(new Long(1));
        List<DishBrandTypeBo> dishBrandTypeBos = this.copy(dishBrandTypes);
        return this.sortBrandTypeByParentId(dishBrandTypeBos);
    }

    @Transactional
    @Override
    public DishBrandTypeBo saveOrUpdate(@NotNull DishBrandTypeBo dishBrandTypeBo) {
        dishBrandTypeBo = this.fillDefaultInfo(dishBrandTypeBo);
        DishBrandType dishBrandType = dishBrandTypeBo.copyTo(DishBrandType.class);
        dishBrandTypeRepository.save(dishBrandType);
        return dishBrandTypeBo;
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public int update(DishBrandTypeBo dishBrandTypeBo) {
        return dishBrandTypeRepository.updateDishBrandType(dishBrandTypeBo.getName(),dishBrandTypeBo.getTypeCode(),dishBrandTypeBo.getId());
    }

    @Override
    public DishBrandTypeBo getDishBrandType(Long id) {
        DishBrandType dishBrandType = dishBrandTypeRepository.findById(id).get();
        return dishBrandType.copyTo(DishBrandTypeBo.class);
    }

    private DishBrandTypeBo fillDefaultInfo(DishBrandTypeBo dishBrandTypeBo){
        String uuid = UUID.randomUUID().toString();
        if(Objects.isNull(dishBrandTypeBo.getUuid())){
            dishBrandTypeBo.setUuid(uuid);
        }
        if(Objects.isNull(dishBrandTypeBo.getSort())){
            dishBrandTypeBo.setSort((long) (Math.random()*9+1)*100000);
        }
        if(Objects.isNull(dishBrandTypeBo.getStatusFlag())){
            dishBrandTypeBo.setStatusFlag(new Long(1));
        }
        if(Objects.isNull(dishBrandTypeBo.getBrandIdenty())){
            dishBrandTypeBo.setBrandIdenty(new Long(1));
        }
        if(Objects.isNull(dishBrandTypeBo.getEnabledFlag())){
            dishBrandTypeBo.setEnabledFlag(new Long(1));
        }
        if(Objects.isNull(dishBrandTypeBo.getIsCure())){
            dishBrandTypeBo.setIsCure(new Long(2));
        }
        if(Objects.isNull(dishBrandTypeBo.getIsOrder())){
            dishBrandTypeBo.setIsOrder(new Long(1));
        }

        return dishBrandTypeBo;
    }


    private List<DishBrandTypeBo> copy(List<DishBrandType> dishBrandTypes){
        if(dishBrandTypes!=null&&!dishBrandTypes.isEmpty()) {
            List<DishBrandTypeBo> dishBrandTypeBos = new ArrayList<DishBrandTypeBo>();
            dishBrandTypes.stream().filter(Objects::nonNull).forEach(dishBrandType->{
                        if(Objects.nonNull(dishBrandType)){
                            dishBrandTypeBos.add(dishBrandType.copyTo(DishBrandTypeBo.class));
                        }
                    }
                    );
            return dishBrandTypeBos;
        }else {
            return null;
        }
    }

    /**
     * 按照一级分类映射二级分类，组装成MAP返回前台显示
     * @param dishBrandTypeBos
     * @return
     */
    private Map<DishBrandTypeBo,List<DishBrandTypeBo>> sortBrandTypeByParentId(List<DishBrandTypeBo> dishBrandTypeBos){
        Map<DishBrandTypeBo,List<DishBrandTypeBo>> map= new HashMap<>();
        if(Objects.isNull(dishBrandTypeBos)||dishBrandTypeBos.size()<1){
            return map;
        }
        List<DishBrandTypeBo> parentDishBrandTypeBo = new ArrayList<>();
        List<DishBrandTypeBo> childrenDishBrandTypeBo = new ArrayList<>();
        for(DishBrandTypeBo bo : dishBrandTypeBos){
            if(Objects.isNull(bo.getParentId())){
                parentDishBrandTypeBo.add(bo);
            }else{
                childrenDishBrandTypeBo.add(bo);
            }
        }
        for(DishBrandTypeBo parentBo:parentDishBrandTypeBo){
            long id = parentBo.getId();
            List<DishBrandTypeBo> children = new ArrayList<>();
            for(DishBrandTypeBo childBo:childrenDishBrandTypeBo){
                long parentId =  childBo.getParentId();
                if(id == parentId){
                    children.add(childBo);
                }
            }
            map.put(parentBo,children);
        }
        return map;
    }

}


