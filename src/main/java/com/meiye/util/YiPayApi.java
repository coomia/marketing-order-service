package com.meiye.util;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.pay.*;
import com.meiye.exception.BusinessException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

/**
 * Created by Administrator on 2018/9/16 0016.
 */
public class YiPayApi {
    static  Logger logger = LoggerFactory.getLogger(YiPayApi.class);
    private final static String SCAN_PAY_API_URL="https://pay.sc.189.cn/haipay/micropay";//扫码支付
    private final static String MICRO_APP_PAY_API_URL ="https://pay.sc.189.cn/haipay/applet-pay";//微信小程序支付
    private final static String SCAN_QRCODE_PAY_API_URL="https://pay.sc.189.cn/haipay/qrcodepay";//扫码支付
    private final static String YIPAY_REFUND_URL="https://pay.sc.189.cn/haipay/refund";
    private final static String YIPAY_STATUS_SYNC_URL="https://pay.sc.189.cn/haipay/refresh";
    private final static String YIPAY_REFUND_STATUS_QUERY_URL="https://pay.sc.189.cn/haipay/bill-refund";
    private final static String MICRO_APP_RETURN_URL="/pay/callback/process";
    private final static String YIPAY_VERSION="V1.0";


    public static ScanPayResponseBo scanPay(StorePaymentParamBo paymentParamBo, Integer totalAmount, String authCode, String outTradeNo,String pay_type,Long payItemId){
        logger.info("Start scan pay for order:"+outTradeNo);
        try {
            ScanPayRequestBo scanPayRequestBo = new ScanPayRequestBo();
            scanPayRequestBo.setAppid(paymentParamBo.getAppid());
            scanPayRequestBo.setTotal_amount(totalAmount);
            scanPayRequestBo.setAuth_code(authCode);
            scanPayRequestBo.setOut_trade_no(outTradeNo);
            scanPayRequestBo.setNonce_str(UUIDUtil.randomUUID());
            scanPayRequestBo.setPay_type(pay_type);
            scanPayRequestBo.setVersion(YIPAY_VERSION);
            if(ObjectUtils.isEmpty(payItemId))
                throw new BusinessException("payItemId lost.");
            scanPayRequestBo.setAttach("payItemId="+payItemId);
            String sortString = SortObjectUtil.getSortString(scanPayRequestBo, new String[]{"sign"},true);
            sortString += "&appsecret=" + paymentParamBo.getAppsecret();
            scanPayRequestBo.setSign(DigestUtils.md5Hex(sortString).toUpperCase());
            String result = HttpClientUtils.postParameters(SCAN_PAY_API_URL, ObjectUtil.objectToMapString("",scanPayRequestBo,true));
            logger.info("Scan pay result for order("+outTradeNo+") is:"+result);
            ScanPayResponseBo responseBo= JSON.parseObject(result,ScanPayResponseBo.class);
            return responseBo;
        }catch (Exception exp){
            logger.error("Scan pay for order:"+outTradeNo+" face error",exp);
            return null;
        }
    }

    public static MicroAppPayResponseBo microAppPay(StorePaymentParamBo paymentParamBo, Integer totalAmount, String outTradeNo, String wechatAppId, String wechatOpenId, String remoteIp,Long paymentItemId){
        logger.info("Start micro pay for order:"+outTradeNo);
        try {
            MicroAppPayRequestBo microAppPayRequestBo = new MicroAppPayRequestBo();
            microAppPayRequestBo.setAppid(paymentParamBo.getAppid());
            microAppPayRequestBo.setTotal_amount(totalAmount);
            microAppPayRequestBo.setOut_trade_no(outTradeNo);
            microAppPayRequestBo.setSub_appid(wechatAppId);
            microAppPayRequestBo.setSub_openid(wechatOpenId);
            microAppPayRequestBo.setSpbill_create_ip(remoteIp);
            microAppPayRequestBo.setReturn_url(paymentParamBo.getContextPath() + MICRO_APP_RETURN_URL+"/"+paymentItemId);
            microAppPayRequestBo.setNonce_str(UUIDUtil.randomUUID());
            microAppPayRequestBo.setVersion(YIPAY_VERSION);
            String sortString = SortObjectUtil.getSortString(microAppPayRequestBo, new String[]{"sign"}, true);
            sortString += "&appsecret=" + paymentParamBo.getAppsecret();
            microAppPayRequestBo.setSign(DigestUtils.md5Hex(sortString).toUpperCase());
            logger.info("Micro pay parameter for order("+outTradeNo+") is:"+SortObjectUtil.getSortString(microAppPayRequestBo,null,false));
            String result = HttpClientUtils.postParameters(MICRO_APP_PAY_API_URL, ObjectUtil.objectToMapString("", microAppPayRequestBo, true));
            logger.info("Micro pay result for order("+outTradeNo+") is:"+result);
            MicroAppPayResponseBo responseBo=JSON.parseObject(result,MicroAppPayResponseBo.class);
            return responseBo;
        }catch (Exception exp){
            logger.error("Micro pay for order:"+outTradeNo+" face error",exp);
            return null;
        }
    }


    public static YiPayRefundResponseBo refund(StorePaymentParamBo paymentParamBo,Integer refundAmount,String outTradeNo,String tradeId,String outRefundNo){
        logger.info("Start refund for order(tradeNo:" + outTradeNo + ",refundNo:" + outRefundNo + ")");
        try {
            YiPayRefundRequestBo requestBo = new YiPayRefundRequestBo();
            requestBo.setAppid(paymentParamBo.getAppid());
            requestBo.setRefund_fee(refundAmount);
            requestBo.setOut_trade_no(outTradeNo);
            requestBo.setTrade_id(tradeId);
            requestBo.setOut_refund_no(outRefundNo);
            requestBo.setNonce_str(UUIDUtil.randomUUID());
            requestBo.setVersion(YIPAY_VERSION);
            String sortString = SortObjectUtil.getSortString(requestBo, new String[]{"sign"}, true);
            sortString += "&appsecret=" + paymentParamBo.getAppsecret();
            requestBo.setSign(DigestUtils.md5Hex(sortString).toUpperCase());

            String result = HttpClientUtils.postParameters(YIPAY_REFUND_URL, ObjectUtil.objectToMapString("", requestBo, true));
            logger.info("Refund result for order(tradeNo:" + outTradeNo + ",refundNo:" + outRefundNo + ") is:" + result);
            YiPayRefundResponseBo responseBo = JSON.parseObject(result, YiPayRefundResponseBo.class);
            return responseBo;
        }catch (Exception exp){
            logger.error("Refund for order(tradeNo:" + outTradeNo + ",refundNo:" + outRefundNo + ") face error",exp);
            return null;
        }
    }


    public static SyncPayStatusResponseBo syncPayStatus(StorePaymentParamBo paymentParamBo,String outTradeNo,String tradeId){
        logger.info("Start sync pay status of order (tradeNo:" + outTradeNo + ",tradeId:" + tradeId + ")");
        try {
            SyncPayStatusRequestBo requestBo = new SyncPayStatusRequestBo();
            requestBo.setAppid(paymentParamBo.getAppid());
            requestBo.setOut_trade_no(outTradeNo);
            requestBo.setTrade_id(tradeId);
            requestBo.setVersion(YIPAY_VERSION);
            requestBo.setNonce_str(UUIDUtil.randomUUID());
            String sortString = SortObjectUtil.getSortString(requestBo, new String[]{"sign"}, true);
            sortString += "&appsecret=" + paymentParamBo.getAppsecret();
            requestBo.setSign(DigestUtils.md5Hex(sortString).toUpperCase());

            String result = HttpClientUtils.postParameters(YIPAY_STATUS_SYNC_URL, ObjectUtil.objectToMapString("", requestBo, true));
            logger.info("End sync pay status of order (tradeNo:" + outTradeNo + ",tradeId:" + tradeId + ") is:" + result);
            SyncPayStatusResponseBo responseBo = JSON.parseObject(result, SyncPayStatusResponseBo.class);
            return responseBo;
        }catch (Exception exp) {
            logger.error("Sync pay status of order(tradeNo:" + outTradeNo + ",tradeId:" + tradeId + ") face error", exp);
            return null;
        }
    }

    public static QueryYiPayRefundStatusResponseBo queryRefundStatus(StorePaymentParamBo storePaymentParamBo,String outRefundTradeNo,String refundTradeId){
        logger.info("Start sync refund status of order (tradeNo:" + outRefundTradeNo + ",tradeId:" + refundTradeId + ")");
        try {
            QueryYiPayRefundStatusRequestBo requestBo = new QueryYiPayRefundStatusRequestBo();
            requestBo.setAppid(storePaymentParamBo.getAppid());
            requestBo.setOut_trade_no(outRefundTradeNo);
            requestBo.setTrade_id(refundTradeId);
            requestBo.setNonce_str(UUIDUtil.randomUUID());
            requestBo.setVersion(YIPAY_VERSION);
            String sortString = SortObjectUtil.getSortString(requestBo, new String[]{"sign"}, true);
            sortString += "&appsecret=" + storePaymentParamBo.getAppsecret();
            requestBo.setSign(DigestUtils.md5Hex(sortString).toUpperCase());
            String result = HttpClientUtils.postParameters(YIPAY_REFUND_STATUS_QUERY_URL, ObjectUtil.objectToMapString("", requestBo, true));
            logger.info("End sync refund status of order (tradeNo:" + outRefundTradeNo + ",tradeId:" + refundTradeId + ") is:" + result);
            QueryYiPayRefundStatusResponseBo responseBo = JSON.parseObject(result, QueryYiPayRefundStatusResponseBo.class);
            return responseBo;
        }catch (Exception exp) {
            logger.error("Sync refund status of order(tradeNo:" + outRefundTradeNo + ",tradeId:" + refundTradeId + ") face error", exp);
            return null;
        }
    }

    public  static ScanQrCodePayResponseBo getQrCodeForPay(StorePaymentParamBo paymentParamBo,String outTradeNo,Integer tradeAmount,Long payItemId){
        logger.info("Start get qr code of order :" + outTradeNo);
        try {
            ScanQrCodePayRequestBo requestBo = new ScanQrCodePayRequestBo();
            requestBo.setAppid(paymentParamBo.getAppid());
            requestBo.setOut_trade_no(outTradeNo);
            requestBo.setTotal_amount(tradeAmount);
            requestBo.setVersion(YIPAY_VERSION);
            if(ObjectUtils.isEmpty(payItemId))
                throw new BusinessException("payItemId lost.");
            requestBo.setAttach("payItemId="+payItemId);
            requestBo.setNonce_str(UUIDUtil.randomUUID());
            requestBo.setReturn_url(paymentParamBo.getContextPath() + MICRO_APP_RETURN_URL+"/"+payItemId);
            String sortString = SortObjectUtil.getSortString(requestBo, new String[]{"sign"}, true);
            sortString += "&appsecret=" + paymentParamBo.getAppsecret();
            requestBo.setSign(DigestUtils.md5Hex(sortString).toUpperCase());
            String result = HttpClientUtils.postParameters(SCAN_QRCODE_PAY_API_URL, ObjectUtil.objectToMapString("", requestBo, true));
            logger.info("End get qr code of order :" + outTradeNo +"is:" + result);
            ScanQrCodePayResponseBo responseBo = JSON.parseObject(result, ScanQrCodePayResponseBo.class);
            return responseBo;
        }catch (Exception exp) {
            logger.error("Get qr code for order :" + outTradeNo+" face exception", exp);
            return null;
        }
    }

    public static Long getPaymentIdFromAttach(String attach){
        try {
            if (ObjectUtils.isEmpty(attach))
                return null;
            String[] attachements = attach.split(";");
            for (String attachment : attachements) {
                String[] params = attachment.split("=");
                if ("payItemId".equalsIgnoreCase(params[0]))
                    return new Long(params[1].trim());
            }
        }catch (Exception exp){
            logger.error("获取PaymentItemId错误:",exp);
        }
        return null;
    }






}
