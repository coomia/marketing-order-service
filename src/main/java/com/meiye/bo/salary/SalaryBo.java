package com.meiye.bo.salary;

import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.trade.TradeUserBo;
import com.meiye.system.util.WebUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Shawns
 * @Date: 2018/10/22 20:59
 * @Version 1.0
 */
@Data
public class SalaryBo  implements Serializable{
    protected Long brandIdenty= WebUtil.getCurrentBrandId();
    protected Long shopIdenty=WebUtil.getCurrentStoreId();
    private Integer userId;
    private String userName;
    private Date starDate;
    private Date endDate;
    private Double baseSalary; //基本工资+岗位工资
    private Double salesCommissions;
    private Double saveCommissions;
    private Double projectCommissions;
    private Double salarySum;


}
