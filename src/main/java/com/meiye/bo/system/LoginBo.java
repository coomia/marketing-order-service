package com.meiye.bo.system;

import lombok.Data;

/**
 * Created by Administrator on 2018/8/15 0015.
 */
@Data
public class LoginBo {
    private String userName;
    private String password;
    private String storeId;
    private String verifyCode;
}
