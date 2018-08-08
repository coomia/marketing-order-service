package com.meiye;

import com.meiye.bo.user.UserBo;
import com.meiye.service.cacheDemo.CacheDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheDemoTest {

    @Autowired
    CacheDemoService cacheDemoService;


    @Test
    public void testCreate(){
      UserBo userBo = UserBo.builder().id(new Long(1)).username("ryner").build();
        cacheDemoService.create(userBo);
    }

    @Test
    public void testFind(){
        for(int i =0;i<= 2;i++){
            cacheDemoService.findById(new Long(1));
        }
    }

    @Test
    public void testCache(){
        addUser1();
        for(int i =0;i<= 2;i++){
            System.out.println("第"+i+"次取user1");
            UserBo userBo =  cacheDemoService.findById(new Long(1));
            System.out.println("第"+i+"次去user1："+userBo.toString());

        }
        addUser2();
        selectUser1();
        batchFind();
        delete1();
        batchFind();
        delete1();
        addUser1();
        batchFind();
        delete1();
        selectUser1();

    }

    private void selectUser1(){
        System.out.println("-----------取user1 开始--------------");
        UserBo userBo =  cacheDemoService.findById(new Long(1));
        System.out.println("-----------取user1 结束--------------");

    }


    private void addUser1(){
        System.out.println("------------成功添加新user1 结束--------------");
        UserBo userBo2 = UserBo.builder().id(new Long(1)).username("ryne").build();
        cacheDemoService.create(userBo2);
        System.out.println("------------成功添加新user1 结束--------------");
    }

    private void addUser2(){
        System.out.println("------------成功添加新user2 结束--------------");
        UserBo userBo2 = UserBo.builder().id(new Long(2)).username("shawn").build();
        cacheDemoService.create(userBo2);
        System.out.println("------------成功添加新user2 结束--------------");
    }


    private void batchFind(){
        System.out.println("-----------批量取user 第一次开始--------------");
        List<UserBo> userBoList =  cacheDemoService.findAll();
        System.out.println("-----------批量取user 第一次结束--------------");

        System.out.println("-----------批量取user 第二次开始--------------");
        List<UserBo> userBoList2 =  cacheDemoService.findAll();
        System.out.println("-----------批量取user 第二次结束--------------");
    }

    private void delete1(){
        System.out.println("-----------删除user1 开始--------------");
       cacheDemoService.delete(new Long(1));
        System.out.println("-----------删除user1结束--------------");
;
    }

}
