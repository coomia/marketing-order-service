package com.meiye.controller.salary;

import com.meiye.bo.salary.SalaryBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.service.Salary.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/10/22 21:16
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/pos/api/salary",produces="application/json;charset=UTF-8")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @PostMapping("/getAllSalary")
    public ResetApiResult getAllSalary(@RequestBody SalaryBo salaryBo){
        try {
            List<SalaryBo> allSalary = salaryService.getAllSalary(salaryBo);
            return ResetApiResult.sucess(allSalary);
        }catch (Exception e){
            throw new BusinessException("获取绩效失败!");
        }
    }
}
