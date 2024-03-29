package com.meiye;

import com.meiye.bo.part.DishPropertyBo;
import com.meiye.bo.part.DishShopBo;
import com.meiye.model.part.DishProperty;
import com.meiye.model.part.DishShop;
import com.meiye.service.part.DishPropertyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DishProportyTest {

    @Autowired
    DishPropertyService dishPropertyService;

    @Test
    public void testSave(){
        DishPropertyBo dishPropertyBo = new DishPropertyBo();
        dishPropertyBo.setName("Test");
        dishPropertyBo.setDishShopId(1L);
        dishPropertyBo.setPropertyKind(1L);
        dishPropertyBo.setReprice(10.0);
        dishPropertyBo.setSort(1L);
        dishPropertyBo.setBrandIdenty(1L);
        dishPropertyBo.setUuid("xxxx");
        dishPropertyBo.setStatusFlag(1);
        dishPropertyBo.setEnabledFlag(1L);
        dishPropertyService.save(dishPropertyBo);
        System.out.println("保存成功！");
    }

    @Test
    public void testGetOne(){
        DishPropertyBo oneById = dishPropertyService.getOneById(17L);
        System.out.println(oneById);
    }

    @Test
    public void testGetList(){
        List<Long> ids = new ArrayList<>();
        ids.add(17L);
        ids.add(18L);
        List<DishPropertyBo> byIds = dishPropertyService.getByIds(ids);
        System.out.println(byIds);
    }

    @Test
    public void testDelList(){
        List<Long> ids = new ArrayList<>();
        ids.add(17L);
        ids.add(18L);
        dishPropertyService.deleteByIds(ids);
        System.out.println();
    }
}
