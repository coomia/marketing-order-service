package com.meiye.controller.posApi;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.system.PosApiResult;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.exception.BusinessException;
import com.meiye.service.posApi.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 22:34 2018/9/1
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/pos/api",produces="application/json;charset=UTF-8")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;

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
        orderService.modifyOrderData(modifyOrderBo);
        OrderResponseDto orderResponseDto =orderService.getOrderResponse(modifyOrderBo.getContent().getTradeRequest().getId());
        return PosApiResult.sucess(orderResponseDto);
    }

    @PostMapping("/addOrderData")
    public PosApiResult addOrderData(@RequestBody OrderRequestDto addOrderRequestDto){
        if(Objects.isNull(addOrderRequestDto)){
            logger.error("下单接口-上传数据为空");
            throw new BusinessException("下单接口-上传数据为空，请检查！");
        }
        logger.info("下单接口-上传json数据："+JSON.toJSON(addOrderRequestDto).toString());
        OrderResponseDto orderResponseDto = orderService.addOrderData(addOrderRequestDto);
        OrderResponseDto orderResponseDtoNew = orderService.getOrderResponse(orderResponseDto.getTrade().getId());
        return PosApiResult.sucess(orderResponseDtoNew);
    }

}
