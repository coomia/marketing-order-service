package com.meiye.bo.booking.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Shawns
 * @Date: 2018/9/24 19:47
 * @Version 1.0
 */
@Data
public class ReadyBookedUserResponseDto implements Serializable{
    private Date startTime;
    private Date endTime;
    private Set<Long> userIds = new HashSet<>();
}
