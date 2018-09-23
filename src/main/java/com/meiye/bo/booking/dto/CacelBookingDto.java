package com.meiye.bo.booking.dto;

import com.meiye.bo.trade.CancelTrade.Content;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 17:05 2018/9/23
 * @Modified By:
 */
@Data
public class CacelBookingDto implements Serializable {

    private String appType;
    private Long shopID;
    private Long brandID;
    private CacelDetailDto content;

}
