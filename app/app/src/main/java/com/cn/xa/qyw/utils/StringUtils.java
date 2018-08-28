package com.cn.xa.qyw.utils;

import com.cn.xa.qyw.entiy.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 409160 on 2016/5/13.
 */
public class StringUtils {
    /**
     * 检测字符串是否为空
     * <p/>
     * 空格，null，"" 一律为空
     * <p/>
     * true ：空 ； false ： 不为空
     */
    public static boolean isEmpty(String str) {

        boolean isEmpty = false;

        if (str == null || "".equals(str) || "".equals(str.trim()) || str.length() < 0 || str.equalsIgnoreCase("null")) {
            isEmpty = true;
        }

        return isEmpty;
    }

    public static boolean isEquals(String str1, String str2) {
        if (str1 != null) {
            return str1.equals(str2);
        } else if (str2 != null) {
            return str2.equals(str1);
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

    /**
     * 检测邮箱地址格式
     */
    public static boolean isEmailActive(String email) {

        boolean isEmailActive = false;
        if (isEmpty(email)) {
            return isEmailActive;
        }

        String regExp = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(email);
        isEmailActive = matcher.matches();

        return isEmailActive;
    }


    /**
     * 检测电话号码格式
     */
    public static boolean isCurrentPassword(String password) {
        boolean isCurrentPassword = false;

        if (!isEmpty(password)) {
            if (password.length() >= 6 && password.length() <= 16) {
                isCurrentPassword = true;
            }
        }

        return isCurrentPassword;
    }

    public static String getUserName(UserInfo info){
        if(!isEmpty(info.getNickName())){
            return info.getNickName();
        }

        if(!isEmpty(info.getTrueName())){
            return info.getTrueName();
        }

        return info.getNickName();
    }

    public static String subPhoneString(String phone){

        String first = phone.substring(0,3);
        String second = phone.substring(8);
        String phone1 = first+"****"+second;

        return phone1;
    }

}
