package com.meiye.util;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.customer.CustomerApiResult;
import com.meiye.bo.internalapi.CardTimesBuyRequestBo;
import com.meiye.bo.internalapi.CardTimesDetailRequestBo;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.TradeBo;
import com.meiye.bo.trade.TradeItemBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.customer.Customer;
import com.meiye.system.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/24 0024.
 */
public class MeiYeInternalApi {
    static Logger logger= LoggerFactory.getLogger(MeiYeInternalApi.class);
//    public final static String MeiYeIntegerApiUrlPrefix="http://172.31.144.129:8080/";
//    public final static String MeiYeIntegerApiUrlPrefix="http://47.105.100.99:8080/marketing";
    public final static String MeiYeIntegerApiUrlPrefix="http://192.168.14.134:8080/marketing";
//    public final static String MeiYeIntegerApiUrlPrefix="http://zhongmeiyunfu.com:8080";

    //核销
    public static WriteOffResultBo writeOff(Long tradeId, Long brandIdenty, Long shopIdenty){
        try {
//            String apiUrl = MeiYeIntegerApiUrlPrefix + String.format("/internal/writeOff/useData?brandIdenty=%s&shopIdenty=%s&tradeId=%s", brandIdenty, shopIdenty, tradeId);
            String apiUrl = MeiYeIntegerApiUrlPrefix + "/internal/writeOff/useData";
            Map<String,Long> params=new HashMap<>();
            params.put("brandIdenty",brandIdenty);
            params.put("shopIdenty",shopIdenty);
            params.put("tradeId",tradeId);
            String result = callInternalApi(apiUrl, params, true, "核销程序",tradeId);
            return JSON.parseObject(result, WriteOffResultBo.class);
        }catch (Exception exp){
            throw new BusinessException("调用核销程序失败",ResetApiResult.STATUS_ERROR,1003);
        }
    }

    //反核销
    public static WriteOffResultBo returnPrivilege(Long tradeId,Long brandIdenty, Long shopIdenty){
        try {
//            String apiUrl = MeiYeIntegerApiUrlPrefix + String.format("/internal/writeOff/returnPrivilege?brandIdenty=%s&shopIdenty=%s&tradeId=%s", brandIdenty, shopIdenty, tradeId);
            String apiUrl = MeiYeIntegerApiUrlPrefix + "/internal/writeOff/returnPrivilege";
            Map<String,Long> params=new HashMap<>();
            params.put("brandIdenty",brandIdenty);
            params.put("shopIdenty",shopIdenty);
            params.put("tradeId",tradeId);

            String result = callInternalApi(apiUrl, params, true, "反核销程序",tradeId);
            return JSON.parseObject(result, WriteOffResultBo.class);
        }catch (Exception exp){
        throw new BusinessException("调用反核销程序失败",ResetApiResult.STATUS_ERROR,1003);
    }
    }

    //会员充值
    public static InternalApiResult recharge(Long customerId, Long tradeId, Long paymentItemId, BigDecimal usefulAmount, Long shopId, Long brandId, Long creatorId, String creatorName){
        try{
            String apiUrl=MeiYeIntegerApiUrlPrefix+"/internal/customer/balance/recharge";
            Map<String,String> params=new HashMap<>();
            params.put("customerId",customerId==null?null:customerId.toString());
            params.put("tradeId",tradeId==null?null:tradeId.toString());
            params.put("paymentItemId",paymentItemId==null?null:paymentItemId.toString());
            params.put("usefulAmount",usefulAmount==null?null:usefulAmount.setScale(2).toString());
            params.put("shopId",shopId==null?null:shopId.toString());
            params.put("brandId",brandId==null?null:brandId.toString());
            params.put("creatorId",creatorId==null?null:creatorId.toString());
            params.put("creatorName",creatorName==null?null:creatorName.toString());
            return JSON.parseObject(callInternalApi(apiUrl,params,true,"会员充值",tradeId),InternalApiResult.class);
        }catch (Exception exp){
            return new InternalApiResult("1001",exp.getMessage());
        }
    }

    //余额消费
    public static InternalApiResult expense(Long customerId, Long tradeId, Long paymentItemId, Double usefulAmount, Long shopId, Long brandId, Long creatorId, String creatorName){
        try{
            String apiUrl=MeiYeIntegerApiUrlPrefix+"/internal/customer/balance/expense";
            Map<String,String> params=new HashMap<>();
            params.put("customerId",customerId==null?null:customerId.toString());
            params.put("tradeId",tradeId==null?null:tradeId.toString());
            params.put("paymentItemId",paymentItemId==null?null:paymentItemId.toString());
            params.put("usefulAmount",usefulAmount==null?null:usefulAmount.toString());
            params.put("shopId",shopId==null?null:shopId.toString());
            params.put("brandId",brandId==null?null:brandId.toString());
            params.put("creatorId",creatorId==null?null:creatorId.toString());
            params.put("creatorName",creatorName==null?null:creatorName.toString());
            return JSON.parseObject(callInternalApi(apiUrl,params,true,"余额消费",tradeId),InternalApiResult.class);
        }catch (Exception exp){
            return new InternalApiResult("1001",exp.getMessage());
        }
    }

    //余额退回
    public static InternalApiResult refund(Long customerId, Long tradeId, Long paymentItemId, Double usefulAmount, Long shopId, Long brandId, Long creatorId, String creatorName){
        try{
            String apiUrl=MeiYeIntegerApiUrlPrefix+"/internal/customer/balance/refund";
            Map<String,String> params=new HashMap<>();
            params.put("customerId",customerId==null?null:customerId.toString());
            params.put("tradeId",tradeId==null?null:tradeId.toString());
            params.put("paymentItemId",paymentItemId==null?null:paymentItemId.toString());
            params.put("usefulAmount",usefulAmount==null?null:usefulAmount.toString());
            params.put("shopId",shopId==null?null:shopId.toString());
            params.put("brandId",brandId==null?null:brandId.toString());
            params.put("creatorId",creatorId==null?null:creatorId.toString());
            params.put("creatorName",creatorName==null?null:creatorName.toString());
            return JSON.parseObject(callInternalApi(apiUrl,params,true,"余额退回",tradeId),InternalApiResult.class);
        }catch (Exception exp){
            return new InternalApiResult("1001",exp.getMessage());
        }
    }


    //消费提成
    public static InternalApiResult addCommission(Long tradeId,Long brandIdenty, Long shopIdenty){
        try {
//            String apiUrl = MeiYeIntegerApiUrlPrefix + String.format("/internal/salesAction/addCommission?brandIdenty=%s&shopIdenty=%s&tradeId=%s", brandIdenty, shopIdenty, tradeId);
            String apiUrl = MeiYeIntegerApiUrlPrefix + "/internal/salesAction/payOverAction";

            Map<String,Long> params=new HashMap<>();
            params.put("brandIdenty",brandIdenty);
            params.put("shopIdenty",shopIdenty);
            params.put("tradeId",tradeId);
            String result = callInternalApi(apiUrl, params, true, "消费提成",tradeId);
            return JSON.parseObject(result, InternalApiResult.class);
        }catch (Exception exp){
            return new InternalApiResult("1001",exp.getMessage());
        }
    }


    //消费提成退回
    public static InternalApiResult subtractCommission(Long tradeId,Long brandIdenty, Long shopIdenty){
        try {
//            String apiUrl = MeiYeIntegerApiUrlPrefix + String.format("/internal/salesAction/subtractCommission?brandIdenty=%s&shopIdenty=%s&tradeId=%s", brandIdenty, shopIdenty, tradeId);
            String apiUrl = MeiYeIntegerApiUrlPrefix + "/internal/salesAction/returnOrverAction";
            Map<String,Long> params=new HashMap<>();
            params.put("brandIdenty",brandIdenty);
            params.put("shopIdenty",shopIdenty);
            params.put("tradeId",tradeId);
            String result = callInternalApi(apiUrl, params, true, "消费提成撤回",tradeId);
            return JSON.parseObject(result, InternalApiResult.class);
        }catch (Exception exp){
            return new InternalApiResult("1001",exp.getMessage());
        }
    }

    //调会员接口
    public static CustomerApiResult registCustomer(String name, String mobile, Long gender, Long tradeId){
        try {
            String apiUrl = MeiYeIntegerApiUrlPrefix + "/marketing/internal/customer/queryOrAdd";
            Map<String,Object> params=new HashMap<>();
            params.put("name",name);
            params.put("mobile",mobile);
            params.put("gender",gender.intValue());
            String result = callInternalApi(apiUrl, params, true, "调会员接口",tradeId);
            return JSON.parseObject(result, CustomerApiResult.class);
        }catch (Exception exp){
            throw new BusinessException("调用会员接口失败",ResetApiResult.STATUS_ERROR,1003);
        }
    }


    public static String callInternalApiByForm(String apiUrl, Map<String,String> params,boolean post,String apiName,Long tradeId){
        try {
            String strParams="";
            if(!ObjectUtils.isEmpty(params))
                strParams=SortObjectUtil.getSortString(params,null,false);
            logger.info("开始为trade("+tradeId+")调用 "+apiName+",Url is:"+apiUrl+",parameter is:"+strParams);
            String result="";
            if(post)
                result=HttpClientUtils.postParameters(apiUrl,params);
            else {
                if(apiUrl.indexOf("?")>0)
                    apiUrl+=strParams;
                else
                    apiUrl+="?"+strParams;
                result = HttpClientUtils.get(apiUrl);
            }
            logger.info("为trade("+tradeId+")调用 "+apiName+" 结束,结果为:"+result);
            return result;
        }catch (Exception exp){
            logger.error("为trade("+tradeId+"调用 "+apiName+"失败",exp);
            throw new BusinessException("调用"+apiName+"失败");
        }
    }


    public static String callInternalApi(String apiUrl, Object params,boolean post,String apiName,Long tradeId){
        try {
            String strParams="";
            if(!ObjectUtils.isEmpty(params))
                strParams=JSON.toJSONString(params, WebUtil.getFastJsonSerializerFeature());
            logger.info("开始为trade("+tradeId+")调用 "+apiName+",Url is:"+apiUrl+",parameter is:"+strParams);
            String result="";
            if(post)
                result=HttpClientUtils.postParameters(apiUrl,strParams,true);
            else {
                if(apiUrl.indexOf("?")>0)
                    apiUrl+=strParams;
                else
                    apiUrl+="?"+strParams;
                result = HttpClientUtils.get(apiUrl);
            }
            logger.info("为trade("+tradeId+")调用 "+apiName+" 结束,结果为:"+result);
            return result;
        }catch (Exception exp){
            logger.error("为trade("+tradeId+"调用 "+apiName+"失败",exp);
            throw new BusinessException("调用"+apiName+"失败");
        }
    }

    //购买次卡
    public static InternalApiResult buyCardTimes(Long customerId, TradeBo tradeBo) {
        try {
            CardTimesBuyRequestBo cardTimesBuyRequestBo = new CardTimesBuyRequestBo();
            cardTimesBuyRequestBo.setBrandId(tradeBo.getBrandIdenty());
            cardTimesBuyRequestBo.setShopId(tradeBo.getShopIdenty());
            cardTimesBuyRequestBo.setCustomerId(customerId);
            cardTimesBuyRequestBo.setTradeId(tradeBo.getId());
            cardTimesBuyRequestBo.setTradeUuid(tradeBo.getUuid());

            List<CardTimesDetailRequestBo> cardTimesDetailRequestBoList = new ArrayList<CardTimesDetailRequestBo>();
            if (!ObjectUtils.isEmpty(tradeBo.getTradeItems())) {
                for (TradeItemBo tradeItemBo : tradeBo.getTradeItems()) {
                    CardTimesDetailRequestBo cardTimesDetailRequestBo = new CardTimesDetailRequestBo();
                    cardTimesDetailRequestBo.setDishId(tradeItemBo.getDishId());
                    cardTimesDetailRequestBo.setDishName(tradeItemBo.getDishName());
                    cardTimesDetailRequestBo.setTradeCount(tradeItemBo.getQuantity());
                    cardTimesDetailRequestBo.setCreatorId(tradeItemBo.getCreatorId());
                    cardTimesDetailRequestBo.setCreatorName(tradeItemBo.getCreatorName());
                    cardTimesDetailRequestBoList.add(cardTimesDetailRequestBo);
                }
            }
            cardTimesBuyRequestBo.setDishs(cardTimesDetailRequestBoList);
            String apiUrl = MeiYeIntegerApiUrlPrefix + "/internal/customer/cardtime/buy";

            String result = callInternalApi(apiUrl, cardTimesBuyRequestBo, true, "次卡充值", tradeBo.getId());
            return JSON.parseObject(result, InternalApiResult.class);
        }catch (Exception exp){
            return new InternalApiResult("1001",exp.getMessage());
        }
    }
}
