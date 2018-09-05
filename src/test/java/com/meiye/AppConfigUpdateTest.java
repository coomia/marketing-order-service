package com.meiye;

import com.meiye.bo.config.AppConfigBo;
import com.meiye.model.config.AppConfig;
import com.meiye.service.config.AppConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/9/2 0002.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppConfigUpdateTest {
    @Autowired
    AppConfigService appConfigService;



}
