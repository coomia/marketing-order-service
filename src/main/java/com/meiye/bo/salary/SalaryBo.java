package com.meiye.bo.salary;

import com.meiye.bo.BusinessParentBo;
import com.meiye.bo.trade.TradeUserBo;
import com.meiye.model.trade.TradeItem;
import com.meiye.system.util.WebUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    private Long userId;
    private String userName;

    private Long roleId;

    private Date startDate;
    private Date endDate;
    private BigDecimal baseSalary = new BigDecimal(0); //基本工资+岗位工资

    private BigDecimal salesCommissions = new BigDecimal(0);
    private BigDecimal salesSum = new BigDecimal(0);
    private String salesCommissionsDetail ="";

    private BigDecimal saveCommissions = new BigDecimal(0);
    private String saveCommissionsDetail ="";
    private BigDecimal saveSum = new BigDecimal(0);

    private BigDecimal projectCommissions = new BigDecimal(0);
    private BigDecimal salarySum = new BigDecimal(0);
    private List<ProjectCommionsDetailBo> ProjectCommionsDetailBos = new ArrayList<>();

    public SalaryBo(){}


}
