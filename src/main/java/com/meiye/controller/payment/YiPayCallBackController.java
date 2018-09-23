package com.meiye.controller.payment;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.pay.YiPayCallBackRequestBo;
import com.meiye.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Controller
@RequestMapping(value = "/pay/callback",produces="application/json;charset=UTF-8")
public class YiPayCallBackController {

    Logger logger= LoggerFactory.getLogger("com.meiye.controller.posApi");

//    @RequestMapping("/process")
//    public void processCallBack(@RequestBody YiPayCallBackRequestBo yiPayCallBackRequestBo,HttpServletResponse response){
//        try {
//            logger.info("Get pay call back for order:" + yiPayCallBackRequestBo.getOut_trade_no() + ",call back parameters is:" + JSON.toJSONString(yiPayCallBackRequestBo));
//            response.getOutputStream().println("sucess");
//
//            //ToDo 支付成功后的业务逻辑
//
//            logger.error("Process pay call back for order:"+ yiPayCallBackRequestBo.getOut_trade_no() +" success");
//        }catch (Exception exp){
//            logger.error("Process pay call back for order:" +yiPayCallBackRequestBo.getOut_trade_no()+" occurred exception ",exp);
//            try{
//                response.getWriter().println("faild");
//            }catch (Exception e){
//                logger.error("Send call back process status to YiPay server face exception:",e);
//            }
//        }
//    }

    @RequestMapping("/process")
    public void processCallBack(HttpServletRequest request, HttpServletResponse response,YiPayCallBackRequestBo yiPayCallBackRequestBo){
        try {
            logger.info("Get pay call back for order:" + yiPayCallBackRequestBo.getOut_trade_no() + ",call back parameters is:" + JSON.toJSONString(yiPayCallBackRequestBo));
            response.getOutputStream().println("sucess");

            //ToDo 支付成功后的业务逻辑

            logger.error("Process pay call back for order:"+ yiPayCallBackRequestBo.getOut_trade_no() +" success");
        }catch (Exception exp){
            logger.error("Process pay call back for order:" +yiPayCallBackRequestBo.getOut_trade_no()+" occurred exception ",exp);
            try{
                response.getWriter().println("faild");
            }catch (Exception e){
                logger.error("Send call back process status to YiPay server face exception:",e);
            }
        }
    }

}
