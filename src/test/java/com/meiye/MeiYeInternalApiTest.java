package com.meiye;

import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.util.MeiYeInternalApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jonyh on 2018/10/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeiYeInternalApiTest {


    @Test
    public void userData(){
        WriteOffResultBo internalApiResult= MeiYeInternalApi.writeOff(147l,1l,1l);
        System.out.println(internalApiResult.toString());

    }

}
