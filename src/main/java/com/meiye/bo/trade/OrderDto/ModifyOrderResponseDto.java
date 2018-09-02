package com.meiye.bo.trade.OrderDto;

import com.meiye.bo.trade.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 15:37 2018/9/2
 * @Modified By:
 */
@Data
public class ModifyOrderResponseDto implements Serializable {

    private TradeBo trade;
    private List<TradeCustomerBo> tradeCustomers;
    private List<TradeItemBo> tradeItems;
    private List<TradePrivilegeBo> tradePrivileges;
    private List<TradeItemPropertyBo> tradeItemProperties;
    private List<TradeUserBo> tradeUsers;
    private List<CustomerCardTimeBo> customerCardTimes;
    private List<TradeTableBo> tables;

}
