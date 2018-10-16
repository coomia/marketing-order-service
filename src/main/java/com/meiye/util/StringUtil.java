package com.meiye.util;

import org.springframework.util.ObjectUtils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Gavin Lei
 *
 * @date 2016年7月9日 下午8:57:48
 */
public class StringUtil {
	public static String str;
	public static final String EMPTY_STRING = "";

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String getCurrentTime(String formate){
		String dateFormate= "yyMMddHH24mmss";
		if(!ObjectUtils.isEmpty(formate))
			dateFormate=formate;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFormate);
		Date now=new Date();
		return simpleDateFormat.format(now);
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b
	 * 		字节数组
	 * @return
	 * 		16进制字串
	 * @author Gavin Lei
	 * @date 2016年7月9日 下午8:57:53
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * MD5 加密
	 * @param origin
	 * 		员是指
	 * @return
	 * @author Gavin Lei
	 * @date 2016年7月9日 下午8:58:13
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}
}
