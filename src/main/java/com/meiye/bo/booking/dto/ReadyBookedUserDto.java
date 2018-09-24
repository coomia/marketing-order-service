package com.meiye.bo.booking.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: Shawns
 * @Date: 2018/9/24 19:37
 * @Version 1.0
 */
@Data
public class ReadyBookedUserDto implements Serializable {
    private Date startTime;
    private Date endTime;
    private Long userType;

}
