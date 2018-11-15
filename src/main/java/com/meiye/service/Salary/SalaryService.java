package com.meiye.service.Salary;

import com.meiye.bo.role.AuthUserBo;
import com.meiye.bo.salary.SalaryBo;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.model.role.AuthUser;
import org.springframework.data.domain.Page;

import java.util.List;


public interface SalaryService {

    List<SalaryBo> getAllSalary(SalaryBo salaryBo);


    SalaryBo getOneSalary(SalaryBo salaryBo);

}