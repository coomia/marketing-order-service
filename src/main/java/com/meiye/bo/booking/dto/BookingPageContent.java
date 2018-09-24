package com.meiye.bo.booking.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Shawns
 * @Date: 2018/9/24 20:40
 * @Version 1.0
 */
@Data
public class BookingPageContent implements Serializable {
    private Date endTime;
    private Date startTime;
    private Integer page;
    private Integer pageCount;
    private Integer type;
}
