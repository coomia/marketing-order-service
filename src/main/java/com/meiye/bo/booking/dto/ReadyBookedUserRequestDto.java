package com.meiye.bo.booking.dto;

import com.meiye.bo.BusinessParentBo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Shawns
 * @Date: 2018/9/24 19:38
 * @Version 1.0
 */
@Data
public class ReadyBookedUserRequestDto implements Serializable {
    private Long shopID;
    private Long brandID;
    private String deviceID;
    private ReadyBookedUserDto content;

}
