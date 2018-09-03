package com.meiye.bo.trade.OrderDto;

import com.meiye.bo.trade.*;
import com.meiye.model.trade.*;
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

    private Trade trade;
    private List<TradeCustomer> tradeCustomers;
    private List<TradeItem> tradeItems;
    private List<TradePrivilege> tradePrivileges;
    private List<TradeItemProperty> tradeItemProperties;
    private List<TradeUser> tradeUsers;
    private List<CustomerCardTime> customerCardTimes;
    private List<TradeTable> tables;

}
