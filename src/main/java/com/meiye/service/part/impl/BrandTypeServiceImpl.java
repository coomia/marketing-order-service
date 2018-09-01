package com.meiye.service.part.impl;

import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.part.DishBrandType;
import com.meiye.repository.part.DishBrandTypeRepository;
import com.meiye.service.part.BrandTypeService;
import com.meiye.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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
       int i =0;
        try{
           i= dishBrandTypeRepository.deleteDishBrandType(Constants.DATA_DISABLE,id);
        }catch (Exception e){
            throw new BusinessException("查找所有分类失败!");
        }
       return i;
    }

    @Override
    public List<DishBrandTypeBo> getAllDishBrandTypeList() {
        List<DishBrandType> dishBrandTypes = null;
        try{
             dishBrandTypes = dishBrandTypeRepository.findAllByStatusFlagOrderBySortDesc(Constants.DATA_ENABLE);
        }catch (Exception e){
            throw new BusinessException("查找所有分类失败!");
        }
        List<DishBrandTypeBo> dishBrandTypeBos = this.copy(dishBrandTypes);
        return this.sortBrandTypeByParentId(dishBrandTypeBos);
    }

    @Transactional
    @Override
    public DishBrandTypeBo saveOrUpdate(@NotNull DishBrandTypeBo dishBrandTypeBo) {
        dishBrandTypeBo = this.fillDefaultInfo(dishBrandTypeBo);
        DishBrandType dishBrandType = dishBrandTypeBo.copyTo(DishBrandType.class);
        dishBrandType.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        try{
            dishBrandTypeRepository.save(dishBrandType);
        }catch (Exception e){
            throw new BusinessException("保存分类失败!");
        }
        return dishBrandTypeBo;
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public int update(DishBrandTypeBo dishBrandTypeBo) {
        String name = dishBrandTypeBo.getName();
        String typeCode = dishBrandTypeBo.getTypeCode();
        if(Objects.isNull(name)||Objects.isNull(typeCode)){
            throw new BusinessException("分类名，分类编码不能为空！");
        }
        return dishBrandTypeRepository.updateDishBrandType(name,typeCode,dishBrandTypeBo.getId());
    }

    @Override
    public DishBrandTypeBo getDishBrandType(Long id) {
        DishBrandType dishBrandType = null;
        try{
             dishBrandType = dishBrandTypeRepository.findById(id).get();
        }catch (Exception e){
            throw new BusinessException("根据ID获取分类失败!");
        }
        return dishBrandType.copyTo(DishBrandTypeBo.class);
    }

    private DishBrandTypeBo fillDefaultInfo(DishBrandTypeBo dishBrandTypeBo){
        String uuid = UUID.randomUUID().toString();
        if(Objects.isNull(dishBrandTypeBo.getUuid())){
            dishBrandTypeBo.setUuid(uuid);
        }
        if(Objects.isNull(dishBrandTypeBo.getSort())){
            Random random = new Random();
            String result="";
            for (int i=0;i<6;i++)
            {
                result+=random.nextInt(10);
            }
            dishBrandTypeBo.setSort(Long.valueOf(result));
        }
        if(Objects.isNull(dishBrandTypeBo.getStatusFlag())){
            dishBrandTypeBo.setStatusFlag(Constants.DATA_ENABLE);
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
    private List<DishBrandTypeBo> sortBrandTypeByParentId(List<DishBrandTypeBo> dishBrandTypeBos){
        Map<DishBrandTypeBo,List<DishBrandTypeBo>> map= new HashMap<>();
        if(Objects.isNull(dishBrandTypeBos)||dishBrandTypeBos.size()<1){
            return null;
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
            parentBo.setDishBrandTypeBoList(children);
        }
        return parentDishBrandTypeBo;
    }

}


