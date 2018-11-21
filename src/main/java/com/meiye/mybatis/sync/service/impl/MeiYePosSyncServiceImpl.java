package com.meiye.mybatis.sync.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meiye.bo.config.PosSyncConfigBo;
import com.meiye.exception.BusinessException;
import com.meiye.mybatis.sync.dao.MeiYePosSyncMapper;
import com.meiye.mybatis.sync.service.MeiYePosSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/9/1 0001.
 */
@Service
public class MeiYePosSyncServiceImpl implements MeiYePosSyncService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MeiYePosSyncMapper meiYePosSyncMapper;

    @Cacheable(value = "app_config")
    public List<PosSyncConfigBo> listAppConfigs(){
        return meiYePosSyncMapper.findAllConfigs();
    }

    public PosSyncConfigBo findConfigByConfigName(String requestTableName){
        List<PosSyncConfigBo> appConfigBos= meiYePosSyncMapper.findAllConfigs();
        PosSyncConfigBo posSyncConfigBo=null;
        if (requestTableName!=null&&appConfigBos!=null) {
            for(PosSyncConfigBo configBo:appConfigBos){
                if(requestTableName.equalsIgnoreCase(configBo.getRequestTable()))
                    return configBo;
            }
        }
        return posSyncConfigBo;
    }

    @Override
    @Async
    public Future<Map<String,Object>> loadSyncData(Long brandId, Long shopId, boolean isInit, Integer pageSize, Long id, Long lastUpdateTime, String tableName){
        try {
            PosSyncConfigBo posSyncConfigBo = findConfigByConfigName(tableName);
            String syncTableName=tableName;
            if(posSyncConfigBo!=null&& !ObjectUtils.isEmpty(posSyncConfigBo.getSyncTable()))
                syncTableName=posSyncConfigBo.getSyncTable();
            Page<HashMap<String, Object>> page = PageHelper.startPage(1, pageSize);
            List<HashMap<String, Object>> result = meiYePosSyncMapper.selectDataByTable(syncTableName, brandId, shopId, isInit, id, lastUpdateTime/1000l, posSyncConfigBo);
            Map<String, Object> resultData = new HashMap<String, Object>();
            boolean completeSearch = false;
            Object lastId = null;
            Long lastUpdateDate = null;
            if (result == null || result.isEmpty()) {
                lastId = id;
                lastUpdateDate = lastUpdateTime;
                completeSearch = true;
            } else {
                if (page.getTotal() <= result.size())
                    completeSearch = true;
                HashMap<String, Object> lastObj = result.get(result.size() - 1);
                lastId = lastObj.get("id");
                if (StringUtils.isEmpty(lastObj.get("serverUpdateTime")) && StringUtils.isEmpty(lastObj.get("serverCreateTime")))
                    lastUpdateDate = 0l;
                else
                    lastUpdateDate = (!StringUtils.isEmpty(lastObj.get("serverUpdateTime")) ? (Date) lastObj.get("serverUpdateTime") : (Date) lastObj.get("serverCreateTime")).getTime();

            }
            resultData.put("lastId", lastId);
            resultData.put("datas", result);
            resultData.put("lastSyncMarker", lastUpdateDate + ":" + lastId);

            Map<String, Object> dataInTable = new HashMap<String, Object>();
            dataInTable.put(tableName, resultData);
            dataInTable.put("completeSearch", completeSearch);
            Future<Map<String, Object>> syncFuture = new AsyncResult<Map<String, Object>>(dataInTable);
            return syncFuture;
        }catch (Exception exp){
            logger.error("Sync data failed for table:"+tableName,exp);
            Map<String, Object> resultData = new HashMap<String, Object>();
            resultData.put("lastId", id);
            resultData.put("datas", new ArrayList<>());
            resultData.put("lastSyncMarker",lastUpdateTime+":"+id);
            Map<String, Object> dataInTable = new HashMap<String, Object>();
            dataInTable.put(tableName, resultData);
            dataInTable.put("completeSearch", true);
            Future<Map<String, Object>> syncFuture = new AsyncResult<Map<String, Object>>(dataInTable);
            return syncFuture;
        }
    }

    @Override
    public HashMap<String,Object> getShopInfoByDeviceMac(String deviceMac){
        return meiYePosSyncMapper.getShopInfoByDeviceMac(deviceMac);
    }




}
