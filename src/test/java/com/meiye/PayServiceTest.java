package com.meiye;

import com.meiye.model.pay.PaymentItem;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PayServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    PayService payService;


    @Test
    public void test(){
        ArrayList<Long> paymentIds=new ArrayList<>();
        paymentIds.add(6l);
        paymentIds.add(8l);
        paymentIds.add(14l);
        List<PaymentItem> paymentItems=payService.findPaymentItemsByPamentId(paymentIds,false);
        System.out.println(paymentIds);
    }
}
