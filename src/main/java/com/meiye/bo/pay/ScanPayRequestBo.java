package com.meiye.bo.pay;

import com.meiye.exception.BusinessException;
import com.meiye.util.SortObjectUtil;
import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * Created by Administrator on 2018/9/15 0015.
 */
@Data
public class ScanPayRequestBo {
    private String appid; //每个商户自己的 appid
    private String sign;//签名
    private Integer total_amount;//支付金额，单位为分
    private Integer discountable_amount;//订单优惠金额,非必需
    private String discount_coupon;//优惠卷编号,非必需
    private String body;//商品名称,非必需
    private String detail;//商品明细,非必需
    private String auth_code;//用户授权码
    private String attach;//附加数据，在查询 API和支付回调通知中原样返回，该字段主要携带订单的自定义数据;如果有门店信息，请添加到此字段中,非必需
    private String pay_type;//支付方式 0：微信 1：支付宝 3：翼支付,如果不传即自动识别,非必需
    private String nonce_str;//随机数，32 位
    private String out_trade_no;//商户订单号,订单号必须唯一，长度 8-32 位
    private String version="V1.0";//版本号 必须 V1.0 版本号
    private String openid;//翼商户系统中的 openid,翼商户系统中用户的openid，非必需
    private String note;//订单备注,最长128
}
