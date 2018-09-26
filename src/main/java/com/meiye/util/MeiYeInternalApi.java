package com.meiye.util;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
public class MeiYeInternalApi {
    static Logger logger= LoggerFactory.getLogger(MeiYeInternalApi.class);
    private final static String MeiYeIntegerApiUrlPrefix="http://localhost:8080/";

    //核销
    public static WriteOffResultBo writeOff(Long tradeId, Long brandIdenty, Long shopIdenty){
        try {
            String apiUrl = MeiYeIntegerApiUrlPrefix + String.format("/writeOff/useData?brandIdenty=%s&shopIdenty=%s&tradeId=%s", brandIdenty, shopIdenty, tradeId);
            logger.info("开始为trade("+tradeId+")调用核销程序："+apiUrl);
            String result=HttpClientUtils.get(apiUrl);
            logger.info("trade("+tradeId+")核销结果："+result);
            return JSON.parseObject(result,WriteOffResultBo.class);
        }catch (Exception exp){
            logger.error("调用核销程序失败",exp);
            throw new BusinessException("调用核销程序失败", ResetApiResult.STATUS_ERROR,1003);
        }
    }

    //反核销
    public static WriteOffResultBo returnPrivilege(Long tradeId,Long brandIdenty, Long shopIdenty){
        try {
            String apiUrl = MeiYeIntegerApiUrlPrefix + String.format("/writeOff/returnPrivilege?brandIdenty=%s&shopIdenty=%s&tradeId=%s", brandIdenty, shopIdenty, tradeId);
            logger.info("开始为trade("+tradeId+")调用反核销程序："+apiUrl);
            String result=HttpClientUtils.get(apiUrl);
            logger.info("trade("+tradeId+")反核销结果："+result);
            return JSON.parseObject(result,WriteOffResultBo.class);
        }catch (Exception exp){
            logger.error("调用反核销程序失败",exp);
            throw new BusinessException("调用反核销程序失败", ResetApiResult.STATUS_ERROR,1003);
        }
    }
}
