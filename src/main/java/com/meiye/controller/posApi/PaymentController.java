package com.meiye.controller.posApi;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.WriteOffResultBo;
import com.meiye.bo.pay.*;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.TradeBo;
import com.meiye.controller.payment.AbstractPayController;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.trade.Trade;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.util.MeiYeInternalApi;
import com.meiye.util.YiPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/9/22 0022.
 */
@RestController
@RequestMapping(value = "/pos/api/pay",produces="application/json;charset=UTF-8")
public class PaymentController extends AbstractPayController {
    Logger logger= LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    PayService payService;
    @Autowired
    OrderService orderService;

    @PostMapping("/scanPay")
    public PosApiResult scanPay(@RequestBody AccountingBo accountingBo, HttpServletRequest request){
        return pay(accountingBo,"ScanPay",request);
    }

    @PostMapping("/qrPay")
    public PosApiResult qrPay(@RequestBody AccountingBo accountingBo, HttpServletRequest request){
        return pay(accountingBo,"ScanQrPay",request);
    }


    @PostMapping("/syncTradeStatus")
    public PosApiResult syncOrderPayment(@RequestBody QueryTradePaymentBo queryTradePaymentBo){
        try {
            if (ObjectUtils.isEmpty(queryTradePaymentBo) || ObjectUtils.isEmpty(queryTradePaymentBo.getContent()))
                return PosApiResult.error(null, 1003, "查询参数错误");
            Long tradeId = queryTradePaymentBo.getContent().getTradeId();
            if (tradeId == null)
                return PosApiResult.error(null, 1003, "查询参数错误");
            StorePaymentParamBo storePaymentParamBo = payService.getStorePaymentParamBo(queryTradePaymentBo.getShopID());
            TradeBo tradeBo = orderService.getTradeByTradeId(tradeId);
            if (ObjectUtils.isEmpty(tradeBo))
                return PosApiResult.error(null, 1003, "订单不存在");
            if (tradeBo.getTradeType() == 1) {
                if (tradeBo.getTradePayStatus().equals(1) || tradeBo.getTradePayStatus().equals(2)) {
                    SyncPayStatusResponseBo syncPayStatusResponseBo = YiPayApi.syncPayStatus(storePaymentParamBo, tradeBo.getTradeNo(), null);
                    if (syncPayStatusResponseBo.isPaySuccess()) {
                        String attach = syncPayStatusResponseBo.getAttach();
                        Long paymentItemId = YiPayApi.getPaymentIdFromAttach(attach);
                        payService.yipaySuccess(syncPayStatusResponseBo.getOut_trade_no(), syncPayStatusResponseBo.getTrade_id(), paymentItemId);
                        Trade trade = orderService.getTradeByTradeNo(syncPayStatusResponseBo.getOut_trade_no());
                        payService.afterPaySucess(trade.getId());
                    } else if (syncPayStatusResponseBo.isRefundSuccess())
                        //TODO refund success logic.
                        System.out.println("请处理退款成功的逻辑");
                }
            } else if (tradeBo.getTradeType() == 2) {
                QueryYiPayRefundStatusResponseBo refundStatusResponseBo = YiPayApi.queryRefundStatus(storePaymentParamBo, tradeBo.getTradeNo(), null);
                if (refundStatusResponseBo.isSuccess()) {
                    if (ObjectUtils.isEmpty(refundStatusResponseBo.getRefund_lists())) {
                        QueryYiPayRefundStatusResponseDetailBo refundStatusResponseDetailBo = refundStatusResponseBo.getRefund_lists().get(0);
                        if ("1".equalsIgnoreCase(refundStatusResponseDetailBo.getRefund_status())) {
                            payService.refundSuccessful(tradeId);
                        } else if ("0".equalsIgnoreCase(refundStatusResponseDetailBo.getRefund_status())) {
                            payService.refundFailed(tradeId);
                        }
                    }

                }
            }
            Map<String, Object> map = super.getOrderPaymentData(tradeId);
            return PosApiResult.sucess(map);
        }catch (BusinessException exp){
            logger.error("同步订单状态失败",exp);
            return PosApiResult.error(null, 1003, "同步订单状态失败:"+exp.getMessage());
        }catch (Exception exp){
            logger.error("同步订单状态失败",exp);
            return PosApiResult.error(null, 1003, "同步订单状态失败");
        }
    }


    @PostMapping("/queryTradeStatus")
    public PosApiResult getOrderPaymentStatus(@RequestBody QueryTradePaymentBo queryTradePaymentBo){
        if(ObjectUtils.isEmpty(queryTradePaymentBo)||ObjectUtils.isEmpty(queryTradePaymentBo.getContent()))
            return PosApiResult.error(null,1003,"查询参数错误");
        Long tradeId=queryTradePaymentBo.getContent().getTradeId();
        if(tradeId==null)
            //TODO　Base on paymentItemId to get tradeId
            logger.debug("请处理通过其它方式查询TradeId的逻辑");
        if(tradeId==null)
            return PosApiResult.error(null,1003,"查询参数错误");
        TradeBo tradeBo=orderService.getTradeByTradeId(tradeId);
        if(ObjectUtils.isEmpty(tradeBo))
            return PosApiResult.error(null,1003,"订单不存在");
        Map<String,Object> map=super.getOrderPaymentData(tradeId);
        return PosApiResult.sucess(map);
    }


}
