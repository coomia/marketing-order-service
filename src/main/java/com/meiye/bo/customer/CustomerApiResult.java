package com.meiye.bo.customer;

import com.meiye.bo.accounting.AbstractInternalApiResult;
import com.meiye.model.customer.Customer;
import lombok.Data;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 22:48 2018/10/30
 * @Modified By:
 */
@Data
public class CustomerApiResult extends AbstractInternalApiResult {
    private Customer data;
}
