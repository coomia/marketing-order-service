package com.meiye.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by jonyh on 2018/10/15.
 */
public class ImageUtil {
    static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    public static String getImageBinary(BufferedImage bufferedImage) {
        try {
            BufferedImage bi = bufferedImage;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpeg", baos);
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            logger.info("生成验证码图片失败",e);
            return null;
        }

    }
}
