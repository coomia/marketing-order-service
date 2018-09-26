package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
@Data
public class ScanQrCodePayResponseBo {
    private String code;
    private String qrcode_url;
    public boolean isSuccess(){
        return "0".equalsIgnoreCase(code);
    }
}
