package com.meiye.bo.booking.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 17:07 2018/9/23
 * @Modified By:
 */
@Data
public class CacelDetailDto implements Serializable {

    private Long cancelOrderUser;
    private Long bookingId;
    private Long brandIdenty;
    private Long shopIdenty;
    private String reason;


}
