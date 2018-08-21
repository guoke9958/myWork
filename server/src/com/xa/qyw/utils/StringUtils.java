package com.xa.qyw.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isEmpty(String str) {

		if (str == null || str.equalsIgnoreCase("null") || "".equals(str) || " ".equals(str)) {
			return true;
		}

		return false;
	}
	
	/**
     * 检测电话号码格式
     */
    public static boolean isTelActive(String telNo) {

       if (telNo != null && telNo.length() == 11) {
            String value = telNo;
            String regExp = "^[1]([38][0-9]{1}|[4][75]|[5][012356789]|[7][0678])[0-9]{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(value);
            return m.find();
        } else {
            return false;
        }
    }
}
