package com.meiye.mybatis.sync.service;

import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public interface MeiYePosSyncService {

    @Async
    Future<Map<String,Object>> loadSyncData(Long brandId, Long shopId, boolean isInit, Integer pageSize, Long id, Long lastUpdateTime, String tableName);

    HashMap<String,Object> getShopInfoByDeviceMac(String deviceMac);
}
