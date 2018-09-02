package com.meiye.controller.pos;

import com.meiye.bo.sync.MeiYePosSyncParams;
import com.meiye.bo.system.PosApiResult;
import com.meiye.mybatis.sync.service.MeiYePosSyncService;
import com.meiye.system.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/9/2 0002.
 */
@RestController
@RequestMapping(value = "/pos/api/sync",produces="application/json;charset=UTF-8")
public class MeiYePosSyncDataController {

    @Autowired
    MeiYePosSyncService meiYePosSyncService;

    @PostMapping("/data")
    public PosApiResult syncData(@RequestBody MeiYePosSyncParams syncParams) throws InterruptedException, ExecutionException {
        Map<String,Object> tableDatas=new HashMap<String,Object>();
        if(syncParams.getContent()!=null&&!syncParams.getContent().keySet().isEmpty()){
            Integer pageSize=(syncParams.getSyncCount()==null?2000:syncParams.getSyncCount())/syncParams.getContent().keySet().size();
            List<Future<Map<String, Object>>> allTablsData = new ArrayList<Future<Map<String, Object>>>();
            for(String tableName:syncParams.getContent().keySet()) {
                while (true) {
                    try {
                        String lastSyncMarker = syncParams.getContent().get(tableName).get("lastSyncMarker");
                        String[] syncMarkers = lastSyncMarker.split(":");
                        Long lastUpdateTime = Long.parseLong(syncMarkers[0]);
                        Long lastUpdateId = Long.parseLong(syncMarkers[1]);
                        Future<Map<String, Object>> tableData = meiYePosSyncService.loadSyncData(WebUtil.getCurrentBrandId(), WebUtil.getCurrentStoreId(), syncParams.getIsInit() == 0, pageSize, lastUpdateId, lastUpdateTime, tableName);
                        allTablsData.add(tableData);
                        break;
                    } catch (TaskRejectedException exp) {
                        exp.printStackTrace();
                        Thread.sleep(50);
                    }
                }
            }
            for(Future<Map<String, Object>> tableData:allTablsData){
                tableDatas.putAll(tableData.get());
            }
        }
        return PosApiResult.sucess(tableDatas);
    }

}
