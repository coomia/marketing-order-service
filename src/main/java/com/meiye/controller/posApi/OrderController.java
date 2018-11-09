package com.meiye.controller.posApi;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.CancelTrade.CancelTradeBo;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.exception.BusinessException;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.setting.Tables;
import com.meiye.model.trade.Trade;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 22:34 2018/9/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/pos/api/order",produces="application/json;charset=UTF-8")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;
    @Autowired
    PayService payService;

    @GetMapping("/testLog")
    public ResetApiResult testLog(){
        logger.info("order controller info ---");
        logger.error("order controller error ---");
        orderService.testLog();
        return ResetApiResult.sucess("");
    }

    @PostMapping("/modifyOrderData")
    public PosApiResult modifyOrderData(@RequestBody OrderRequestDto modifyOrderBo){
        if(Objects.isNull(modifyOrderBo)){
            logger.error("改单接口-上传数据为空");
            throw new BusinessException("改单接口-上传数据为空，请检查！");
        }
        logger.info("改单接口-上传json数据："+JSON.toJSON(modifyOrderBo).toString());
        try {
            orderService.modifyOrderData(modifyOrderBo);
            OrderResponseDto orderResponseDto =orderService.getOrderResponse(modifyOrderBo.getContent().getTradeRequest().getId(),true);
            return PosApiResult.sucess(orderResponseDto);
        }catch (BusinessException b){
            if(ObjectUtil.equals( b.getStatusCode(),ResetApiResult.POS_TRADE_CHANGED)) {
                logger.info("改单接口 - 改单失败",b);
                return PosApiResult.error(null, b.getStatusCode(), b.getMessage());
            }
            else
                throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("改单接口- 改单失败！");
        }
    }

    @PostMapping("/addOrderData")
    public PosApiResult addOrderData(@RequestBody OrderRequestDto addOrderRequestDto){
        if(Objects.isNull(addOrderRequestDto)){
            logger.error("下单接口-上传数据为空");
            throw new BusinessException("下单接口-上传数据为空，请检查！");
        }
        logger.info("下单接口-上传json数据："+JSON.toJSON(addOrderRequestDto).toString());
        try {
            OrderResponseDto orderResponseDto = orderService.addOrderData(addOrderRequestDto);
            OrderResponseDto orderResponseDtoNew = orderService.getOrderResponse(orderResponseDto.getTrade().getId(),false);
            return PosApiResult.sucess(orderResponseDtoNew);
        }catch (BusinessException b){
            logger.info("下单接口- 下单失败！",b);
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            logger.info("下单接口- 下单失败！",e);
            throw new BusinessException("下单接口- 下单失败！");
        }
    }

    //作废订单
    @PostMapping("/deleteOrderData")
    public PosApiResult deleteOrderData(@RequestBody CancelTradeBo cancelTradeBo){
        try {
            OrderResponseDto orderResponseDto = orderService.deleteTrade(cancelTradeBo);
            return PosApiResult.sucess(orderService.getOrderResponse(cancelTradeBo.getContent().getObsoleteRequest().getTradeId(), false));
        }catch (BusinessException b){
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("作废订单接口- 作废订单失败！");
        }


    }

    //退换货订单
    @PostMapping("/returnOrderData")
    public PosApiResult returnOrderData(@RequestBody CancelTradeBo cancelTradeBo){
        try {
            Long newTradeId = orderService.returnTrade(cancelTradeBo);
            payService.updateRefundTradeStatus(newTradeId);
//            OrderResponseDto orderResponseDto=orderService.getOrderResponse(newTradeId, false);
            return PosApiResult.sucess(getOrderWithPaymentData(newTradeId));
        }catch (BusinessException b){
            logger.info("退货失败：",b);
            throw new BusinessException(b.getMessage());
        }catch (Exception e){
            throw new BusinessException("退换订单接口- 退换订单失败！");
        }

    }


    public Map<String,Object> getOrderWithPaymentData(Long tradeId){
        Map<String,Object> result=new HashMap<>();
        OrderResponseDto orderResponseDto=orderService.getOrderResponse(tradeId,false);
        if(orderResponseDto!=null) {
            result.put("trade", orderResponseDto.getTrade());
            result.put("tradeTables", orderResponseDto.getTradeTables());
            result.put("tables", ObjectUtils.isEmpty(orderResponseDto.getTables()) ? new ArrayList<Tables>() : orderResponseDto.getTables());
            result.put("tradeItems", orderResponseDto.getTradeItems());
            result.put("tradeCustomers", orderResponseDto.getTradeCustomers());
            result.put("tradePrivileges", orderResponseDto.getTradePrivileges());
            result.put("tradeItemProperties", orderResponseDto.getTradeItemProperties());
            result.put("tradeUsers", orderResponseDto.getTradeUsers());
            result.put("customerCardTimes", orderResponseDto.getCustomerCardTimes());

            List<Payment> payments = payService.findPaymentsByTradeId(tradeId, false);
            result.put("payments", payments);
            if (!ObjectUtils.isEmpty(payments)) {
                List<Long> paymentIds = payments.stream().map(Payment::getId).distinct().collect(Collectors.toList());
                List<PaymentItem> paymentItems = payService.findPaymentItemsByPamentId(paymentIds, false);
                result.put("paymentItems", paymentItems);
                if (!ObjectUtils.isEmpty(paymentItems)) {
                    List<Long> paymentItemIds = paymentItems.stream().map(PaymentItem::getId).distinct().collect(Collectors.toList());
                    List<PaymentItemExtra> paymentItemExtras = payService.findPaymentItemExtraByPamentItemId(paymentItemIds, false);
                    result.put("paymentItemExtras", paymentItemExtras);
                } else
                    result.put("paymentItemExtras", new ArrayList<>());
            } else {
                result.put("paymentItemExtras", new ArrayList<>());
                result.put("paymentItems", new ArrayList<>());
            }
        }
        return result;
    }
}
