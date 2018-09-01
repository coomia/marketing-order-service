package com.meiye;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meiye.mybatis.sync.dao.MeiYePosSyncMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/9/1 0001.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeiYePosSyncDataTest {
    @Autowired
    MeiYePosSyncMapper meiYePosSyncMapper;

    @Test
    public void loadData(){
        Page<HashMap<String,Object>> page= PageHelper.startPage(1,10);
    }

}
