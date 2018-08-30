package com.meiye.controller.order;


import org.apache.log4j.Logger;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 20:43 2018/8/29
 * @Modified By:
 */
public class TestOrderLog {

    public static void main(String[] args) {
        Logger log = Logger.getLogger(TestOrderLog.class);
        log.info("info -- com.meiye.controller.order");
        log.error("error -- com.meiye.controller.order");

    }

}
