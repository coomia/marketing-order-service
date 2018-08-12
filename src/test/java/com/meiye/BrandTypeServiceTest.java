package com.meiye;

import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.part.DishBrandTypeBo;
import com.meiye.bo.user.UserBo;
import com.meiye.service.config.AppConfigService;
import com.meiye.service.part.BrandTypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 20:22 2018/8/12
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrandTypeServiceTest {

    @Autowired
    BrandTypeService brandTypeService;


    @Autowired
    AppConfigService appConfigService;


    @Test
    public void testCreate(){
//        DishBrandTypeBo bo = DishBrandTypeBo.builder().name("保健").typeCode("123456").build();
        DishBrandTypeBo bo = DishBrandTypeBo.builder().parentId(new Long(11)).name("肾部保养").typeCode("123456").build();
        brandTypeService.saveOrUpdate(bo);
    }

    @Test
    public void testUpdate(){
        DishBrandTypeBo bo = DishBrandTypeBo.builder().id(new Long(9)).parentId(new Long(8)).name("水光针护肤保养-update").typeCode("123456").build();
        brandTypeService.update(bo);
    }

    @Test
    public void testDelete(){
        brandTypeService.delete(new Long(9));
    }

    @Test
    public void findAll(){
        Map<DishBrandTypeBo, List<DishBrandTypeBo>> allDishBrandTypeMap = brandTypeService.getAllDishBrandTypeList();
        System.out.println("查询结果：");
        for(DishBrandTypeBo parent :allDishBrandTypeMap.keySet()){
            System.out.println("一级菜单ID："+parent.getId());
            List<DishBrandTypeBo> children = allDishBrandTypeMap.get(parent);
            if(Objects.nonNull(children)){
                for(DishBrandTypeBo child : children){
                    System.out.println("二级菜单父ID:"+child.getParentId());
                }
            }else{
                System.out.println("无二级菜单");
            }
        }
    }


    @Test
    public void testGetOneById(){
        DishBrandTypeBo dishBrandType = brandTypeService.getDishBrandType(new Long(9));
        System.out.println("s");
    }
}
