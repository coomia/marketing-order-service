package com.meiye.mybatis.sync.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.config.PosSyncConfigBo;
import com.meiye.mybatis.sync.dao.MeiYePosSyncMapper;
import com.meiye.mybatis.sync.service.MeiYePosSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/9/1 0001.
 */
@Service
public class MeiYePosSyncServiceImpl implements MeiYePosSyncService {
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

    @Async
    public Future<Map<String,Object>> loadSyncData(Long brandId,Long shopId,boolean isInit, Integer pageSize, Long id, Date lastUpdateTime,String tableName){
        PosSyncConfigBo posSyncConfigBo=findConfigByConfigName(tableName);
        Page<HashMap<String,Object>> page= PageHelper.startPage(1,pageSize);
        List<HashMap<String,Object>> result= meiYePosSyncMapper.selectDataByTable(brandId,shopId,id,lastUpdateTime,posSyncConfigBo);
        Map<String,Object> resultData=new HashMap<String,Object>();
        boolean completeSearch=false;
        Object lastId=null;
        Date lastUpdateDate=null;
        if(result==null||result.isEmpty()){
            lastId=id;
            lastUpdateDate=lastUpdateTime;
            completeSearch=true;
        }else {
            if(page.getTotal()<=result.size())
                completeSearch=true;
            HashMap<String,Object> lastObj=result.get(result.size()-1);
            lastId=lastObj.get("id");
            lastUpdateDate=lastObj.get("serverUpdateTime")!=null?(Date) lastObj.get("serverUpdateTime"):(Date) lastObj.get("serverCreateTime");

        }
        resultData.put("lastId",lastId);
        resultData.put("datas",result);
        resultData.put("lastSyncMarker",lastUpdateDate.getTime()+":"+lastId);
        Future< Map<String,Object>> syncFuture=new AsyncResult<Map<String,Object>>(resultData);
        return syncFuture;
    }



}