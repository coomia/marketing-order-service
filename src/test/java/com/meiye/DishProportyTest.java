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
        dishPropertyService.save(dishPropertyBo);
        System.out.println("保存成功！");
    }

}
