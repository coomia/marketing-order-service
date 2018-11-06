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
    private Date starDate;
    private Date endDate;
    private BigDecimal baseSalary; //基本工资+岗位工资
    private BigDecimal salesCommissions;
    private String salesCommissionsDetail;
    private BigDecimal saveCommissions;
    private String saveCommissionsDetail;
    private BigDecimal projectCommissions;
    private BigDecimal salarySum;
    private List<ProjectCommionsDetailBo> ProjectCommionsDetailBos = new ArrayList<>();

    public SalaryBo(Long brandIdenty, Long shopIdenty, Long userId, String userName, Date starDate, Date endDate, BigDecimal baseSalary) {
        this.brandIdenty = brandIdenty;
        this.shopIdenty = shopIdenty;
        this.userId = userId;
        this.userName = userName;
        this.starDate = starDate;
        this.endDate = endDate;
        this.baseSalary = baseSalary == null? new BigDecimal(0):baseSalary;
    }

    public SalaryBo(){}


}
