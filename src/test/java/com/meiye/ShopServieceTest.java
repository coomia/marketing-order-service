package com.meiye;

import com.meiye.bo.part.DishPropertyBo;
import com.meiye.bo.part.DishShopBo;
import com.meiye.model.part.DishShop;
import com.meiye.service.part.DishShopService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:55 2018/8/12
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopServieceTest {

    @Autowired
    DishShopService dishShopService;

    @Autowired

    @Test
    public void testPageView(){
        Integer page = 10;
        Integer size = 10;
        DishShopBo bo = new DishShopBo();
        bo.setName("ryne");
        bo.setDishTypeId(new Long(1));
        Page<DishShop> pageShop = dishShopService.getDishShopPageByCriteria(page,size,bo);
        System.out.println(pageShop);

    }

    @Test
    public void testGetone(){
        DishShopBo dishShopById = dishShopService.getDishShopById(3L);
        System.out.println(dishShopById);
    }
    @Test
    public void testSave(){
        DishShopBo dishShopById = dishShopService.getDishShopById(4L);

        dishShopById.setId(null);
        List<DishPropertyBo> dishPropertyBos = dishShopById.getDishPropertyBos();
        for (DishPropertyBo dishPropertyBo:dishPropertyBos
             ) {
            dishPropertyBo.setId(null);
        }
        dishShopService.saveDishShop(dishShopById);

    }


}
