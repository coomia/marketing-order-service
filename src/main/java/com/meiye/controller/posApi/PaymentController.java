package com.meiye.controller.posApi;

import com.meiye.bo.accounting.AccountingBo;
import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.pay.*;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.bo.trade.TradeBo;
import com.meiye.controller.payment.AbstractPayController;
import com.meiye.exception.BusinessException;
import com.meiye.model.trade.Trade;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.system.util.WebUtil;
import com.meiye.util.MeiYeInternalApi;
import com.meiye.util.ObjectUtil;
import com.meiye.util.YiPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
        return pay(accountingBo,"ScanPay");
    }

    @PostMapping("/qrPay")
    public PosApiResult qrPay(@RequestBody AccountingBo accountingBo, HttpServletRequest request){
        return pay(accountingBo,"ScanQrPay");
    }


    @GetMapping("/refund/{paymentItemId}")
    public PosApiResult refundPayment(@PathVariable Long paymentItemId){
        try {
            Trade trade=payService.refundPayment(paymentItemId);
            if(ObjectUtil.equals(trade.getTradeType(),2))
                payService.updateRefundTradeStatus(trade.getRelateTradeId());
            OrderResponseDto orderResponseDto=orderService.getOrderResponse(trade.getId(), false);
            return PosApiResult.sucess(orderResponseDto);
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("退换订单接口- 退换订单失败！");
        }
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
                List<PaymentItemBo> paymentItemBos = null;
                if (tradeBo.getTradePayStatus().equals(1) || tradeBo.getTradePayStatus().equals(2)) {
                    paymentItemBos = payService.getAllPaymentItemListByTradeId(tradeId, null);
                } else if(!ObjectUtils.isEmpty(queryTradePaymentBo.getContent().getPaymentItemIds()))
                    paymentItemBos = payService.getAllPaymentItemListByTradeId(tradeId, queryTradePaymentBo.getContent().getPaymentItemIds());
                if(!ObjectUtils.isEmpty(paymentItemBos)) {
                    for (PaymentItemBo paymentItem : paymentItemBos) {
                        if(ObjectUtil.equals(paymentItem.getPayStatus(),1)||ObjectUtil.equals(paymentItem.getPayStatus(),2)) {
                            SyncPayStatusResponseBo syncPayStatusResponseBo = YiPayApi.syncPayStatus(storePaymentParamBo, paymentItem.getUuid(), null);
                            if (syncPayStatusResponseBo.isPaySuccess()) {
                                payService.paySuccess(syncPayStatusResponseBo.getOut_trade_no(), syncPayStatusResponseBo.getTrade_id());
                            }else if(syncPayStatusResponseBo.isRefundSuccess()){
                                payService.updateRefundStatus(paymentItem.getId(),true);
                            }else if(syncPayStatusResponseBo.isPayFailed()){
                                payService.payFailed(syncPayStatusResponseBo.getOut_trade_no());
                            }
                        }
                        if(ObjectUtil.equals(paymentItem.getPayStatus(),4)){ //重复支付发起退货
                            syncRefundPayItemStatus(paymentItem,tradeBo,storePaymentParamBo);
                        }
                    }
                }
            } else if (tradeBo.getTradeType() == 2) {
                List<PaymentItemBo> paymentItemBos=payService.getAllPaymentItemListByTradeId(tradeId,queryTradePaymentBo.getContent().getPaymentItemIds());
                if(!ObjectUtils.isEmpty(paymentItemBos)){
                    for(PaymentItemBo paymentItem:paymentItemBos) {
                        syncRefundPayItemStatus(paymentItem,tradeBo,storePaymentParamBo);
                    }
                }
                payService.updateRefundTradeStatus(tradeId);
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

    private void syncRefundPayItemStatus(PaymentItemBo paymentItem, TradeBo trade,StorePaymentParamBo storePaymentParamBo){
        if (ObjectUtil.equals(paymentItem.getPayStatus(), 3) || ObjectUtil.equals(paymentItem.getPayStatus(), 4)) {
            if (ObjectUtil.equals(paymentItem.getPayModeId(), 1l)) {
                InternalApiResult internalApiResult = null;
                internalApiResult = MeiYeInternalApi.refund(orderService.getCustomerIdByType(trade.getRelateTradeId(), 3), trade.getRelateTradeId(), paymentItem.getId(), paymentItem.getUsefulAmount(), WebUtil.getCurrentStoreId(), WebUtil.getCurrentBrandId(), paymentItem.getCreatorId(), paymentItem.getCreatorName());
                if (internalApiResult.isSuccess()) {
                    payService.updateRefundStatus(paymentItem.getId(), true);
                } else {
                    payService.updateRefundStatus(paymentItem.getId(), false);
                }
            } else if (ObjectUtil.equals(paymentItem.getPayModeId(), 2l)) {
                payService.updateRefundStatus(paymentItem.getId(), true);
            } else if (ObjectUtil.equals(paymentItem.getPayModeId(), 4l) || ObjectUtil.equals(paymentItem.getPayModeId(), 5l)) {
                PaymentItemExtraBo paymentItemExtraBo = ObjectUtils.isEmpty(paymentItem.getPaymentItemExtra()) ? null : paymentItem.getPaymentItemExtra().get(0);
                QueryYiPayRefundStatusResponseBo queryYiPayRefundStatusResponseBo = YiPayApi.queryRefundStatus(storePaymentParamBo, paymentItem.getReturnCode(), paymentItemExtraBo == null ? null : paymentItemExtraBo.getRefundTradeNo());
                if (queryYiPayRefundStatusResponseBo.refundStatus() < 0)
                    payService.updateRefundStatus(paymentItem.getId(), false);
                else if (queryYiPayRefundStatusResponseBo.refundStatus() == 0)
                    payService.updateRefundStatus(paymentItem.getId(), true);
            }
        }
    }


    @PostMapping("/queryTradeStatus")
    public PosApiResult getOrderPaymentStatus(@RequestBody QueryTradePaymentBo queryTradePaymentBo){
        if(ObjectUtils.isEmpty(queryTradePaymentBo)||ObjectUtils.isEmpty(queryTradePaymentBo.getContent()))
            return PosApiResult.error(null,1003,"查询参数错误");
        Long tradeId=queryTradePaymentBo.getContent().getTradeId();
        if(tradeId==null)
            return PosApiResult.error(null,1003,"查询参数错误");
        TradeBo tradeBo=orderService.getTradeByTradeId(tradeId);
        if(ObjectUtils.isEmpty(tradeBo))
            return PosApiResult.error(null,1003,"订单不存在");
        Map<String,Object> map=super.getOrderPaymentData(tradeId);
        return PosApiResult.sucess(map);
    }


}
