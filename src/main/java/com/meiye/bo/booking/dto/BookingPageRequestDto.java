package com.meiye.bo.booking.dto;

import com.meiye.bo.booking.BookingBo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 19:10 2018/9/8
 * @Modified By:
 */
@Data
public class BookingPageRequestDto implements Serializable {

    private Long shopID;
    private Long brandID;
    private String deviceID;
    private String versionCode;
    private String versionName;
    private Integer appType;
    private String systemType;
    private String opVersionUUID;
    private BookingPageContent content;

}
