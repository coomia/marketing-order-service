package com.meiye.controller.system;

import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.system.util.WebUtil;
import com.meiye.util.HttpClientUtils;
import com.meiye.util.MeiYeInternalApi;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/10/11 0011.
 */
@RestController
@RequestMapping(value = "/public/api/thirdSys",produces="application/json;charset=UTF-8")
public class RedirectController {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/redirect/{redirectType}")
    public ResetApiResult redirectByType(@PathVariable String redirectType){
        try{
            String html="";
            Long brandId=WebUtil.getCurrentBrandId();
            Long shopId=WebUtil.getCurrentStoreId();
            Long userId=WebUtil.getCurrentUser().getId();
            String userName=WebUtil.getCurrentUser().getUsername();
            String url= MeiYeInternalApi.MeiYeIntegerApiUrlPrefix;
            if("index".equalsIgnoreCase(redirectType)){
                url+=String.format("/internal/report/main?brandIdenty=%s&shopIdenty=%s&creatorId=%s&creatorName=%s",brandId,shopId,userId,userName);
                html= HttpClientUtils.get(url);
            }else if("report".equalsIgnoreCase(redirectType)){
                url+=String.format("/internal/report/salesReport?brandIdenty=%s&shopIdenty=%s&creatorId=%s&creatorName=%s",brandId,shopId,userId,userName);
                html= HttpClientUtils.get(url);
            }else if("marketing".equalsIgnoreCase(redirectType)){
                url+=String.format("/internal?brandIdenty=%s&shopIdenty=%s&creatorId=%s&creatorName=%s",brandId,shopId,userId,userName);
                html= HttpClientUtils.get(url);
            }else if("customer".equalsIgnoreCase(redirectType)){
                url+=String.format("/internal/customer?brandIdenty=%s&shopIdenty=%s&creatorId=%s&creatorName=%s",brandId,shopId,userId,userName);
                html= HttpClientUtils.get(url);
            }else if("commercialSetting".equalsIgnoreCase(redirectType)){
                url+=String.format("/internal/commercial/settingPage?brandIdenty=%s&shopIdenty=%s&creatorId=%s&creatorName=%s",brandId,shopId,userId,userName);
                html= HttpClientUtils.get(url);
            }else if("customerSetting".equalsIgnoreCase(redirectType)){
                url+=String.format("/internal/customerLevelRule/gotoPage?brandIdenty=%s&shopIdenty=%s&creatorId=%s&creatorName=%s",brandId,shopId,userId,userName);
                html= HttpClientUtils.get(url);
            }else{
                throw new BusinessException("页面不存在");
            }
            ResetApiResult resetApiResult= ResetApiResult.sucess(html);
            resetApiResult.setDataType("HTML");
            return resetApiResult;
        }catch (BusinessException exp){
            logger.error("请求内部页面失败",exp);
            ResetApiResult resetApiResult= ResetApiResult.error(null,exp.getMessage());
            resetApiResult.setStatusCode(ResetApiResult.STATUS_CODE_404);
            return resetApiResult;
        } catch (Exception exp){
            logger.error("请求内部页面失败",exp);
            ResetApiResult resetApiResult= ResetApiResult.error(null,"跳转页面失败");
            resetApiResult.setStatusCode(ResetApiResult.STATUS_CODE_500);
            return resetApiResult;
        }
    }
}
