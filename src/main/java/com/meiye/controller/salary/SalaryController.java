package com.meiye.controller.salary;

import com.meiye.bo.salary.SalaryBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.controller.part.DishShopController;
import com.meiye.exception.BusinessException;
import com.meiye.service.Salary.SalaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/10/22 21:16
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/api/salary",produces="application/json;charset=UTF-8")
public class SalaryController {
    static Logger logger= LoggerFactory.getLogger(DishShopController.class);

    @Autowired
    SalaryService salaryService;

    @GetMapping("/getAllSalary")
    public ResetApiResult getAllSalary(Long shopId, Long brandId, Long startDate, Long endDate){
        SalaryBo salaryBo = new SalaryBo();
        salaryBo.setBrandIdenty(brandId);
        salaryBo.setShopIdenty(shopId);
        salaryBo.setStartDate(new Date(startDate));
        salaryBo.setEndDate(new Date(endDate));
        try {
            List<SalaryBo> allSalary = salaryService.getAllSalary(salaryBo);
            return ResetApiResult.sucess(allSalary);
        }catch (Exception e){
            logger.error("获取绩效失败!",e);
            throw new BusinessException("获取绩效失败!");
        }
    }

    @GetMapping("/getOneSalary")
    public ResetApiResult getOneSalary(Long shopId, Long brandId, Long startDate, Long endDate,Long userId){
        SalaryBo salaryBo = new SalaryBo();
        salaryBo.setBrandIdenty(brandId);
        salaryBo.setShopIdenty(shopId);
        salaryBo.setStartDate(new Date(startDate));
        salaryBo.setEndDate(new Date(endDate));
        salaryBo.setUserId(userId);
        try {
            SalaryBo oneSalary = salaryService.getOneSalary(salaryBo);
            return ResetApiResult.sucess(oneSalary);
        }catch (Exception e){
            logger.error("获取绩效失败!",e);
            throw new BusinessException("获取绩效失败!");
        }
    }
}
