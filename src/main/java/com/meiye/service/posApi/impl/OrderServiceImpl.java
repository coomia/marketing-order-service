package com.meiye.service.posApi.impl;

import com.meiye.service.posApi.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:27 2018/9/1
 * @Modified By:
 */
@Service
public class OrderServiceImpl implements OrderService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void testLog() {
        logger.info("order service info ---");
        logger.error("order service error ---");
    }
}
