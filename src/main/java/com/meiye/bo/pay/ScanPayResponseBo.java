package com.meiye.bo.pay;

import lombok.Data;

/**
 * Created by Administrator on 2018/9/16 0016.
 */
@Data
public class ScanPayResponseBo {
    private String code;//具体返回值如下1、0（成功）2、100（系统错误）3、200（未被授权）4、201（签名不正确）5、202（功能未开通）6、300（参数有误）7、301（其它错误）8、302（支付异常）9、303（请求超时,请同步订单）10、304（用户授权码不能为空）11、305（支付方式有误）12、306（支付金额不能0）13、311（APPID 有误,当 code 为 0 时返回其他信息
    private String trade_state;//交易状态 0：未支付 1：支付成功 2：支付失败 3：已退款 4：已撤销 5：部分退款 6：已关闭
    private String trade_id;//翼商户系统订单号
    private String err_msg;//错误提示
    private String pay_time;//支付时间

    public boolean isSuccess(){
        return "0".equalsIgnoreCase(code);
    }

    public boolean isPaySuccess(){
        return isSuccess()&&"1".equalsIgnoreCase(trade_state);
    }
}
