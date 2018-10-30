package com.meiye.bo.booking.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 17:21 2018/9/23
 * @Modified By:
 */
@Data
public class WxBookingContentDto implements Serializable {

    private String bookingUuid;
    private Long bookingId;
    private Long bookingServerUpdateTime;
    private Integer toOrderStatus;

}
