package com.meiye.bo.trade.OrderDto;

import com.meiye.bo.trade.TradeBo;
import com.meiye.bo.user.UserBo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:38 2018/9/2
 * @Modified By:
 */
@Data
public class AddOrderRequestDto implements Serializable {

    private Long shopID;
    private Long brandID;
    private Integer versionCode;
    private String deviceID;
    private String versionName;
    private String systemType;
    private int appType;
    private TradeBo content;

}
