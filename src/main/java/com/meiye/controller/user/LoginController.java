package com.meiye.controller.user;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.meiye.bo.system.ResetApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/8/6 0006.
 */

@RestController
public class LoginController {
    @Autowired
    DefaultKaptcha defaultKaptcha;


    @PostMapping(value="/login",produces="application/json;charset=UTF-8")
    public ResetApiResult login(){
        return ResetApiResult.sucess("");
    }


    @GetMapping(value="/get/verifyCode")
    public ResetApiResult getVerifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("rightCode", createText);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return ResetApiResult.error(null,"获取验证码失败");
        }

        return null;

    }
}
