package com.meiye.controller.user;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.meiye.bo.system.ResetApiResult;
import com.meiye.exception.BusinessException;
import com.meiye.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/15 0015.
 */
@RestController
@RequestMapping(value = "/verifyCode",produces="application/json;charset=UTF-8")
public class VerifyCodeController {

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @GetMapping(value="/get")
    public ResetApiResult getVerifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        Map<String,String> result=new HashMap<>();
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            result.put("verifyCode",createText);
            httpServletRequest.getSession().setAttribute("rightCode", createText);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            String verifyImage= ImageUtil.getImageBinary(challenge);
            if(verifyImage==null)
                throw new BusinessException("生成验证码失败");
            result.put("verifyImage", ImageUtil.getImageBinary(challenge));
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return ResetApiResult.error(null,"获取验证码失败");
        }

        return ResetApiResult.sucess(result);

    }
}
