package com.xa.qyw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {

	public static void main(String[] args) {
		getPhoneNumber("鍘傚鐩撮攢鍙互0棣栦粯0棣栦粯鍒嗘湡鍚勭璺戣溅鍚勭韪忔澘瀹炰綋搴椾笓鍗�鈥嬫湰搴楁嫢鏈夊悇绉嶆鍨嬮叿杞︼紝璺戣溅锛屾敮鎸佸垎鏈熶粯娆�棣栦粯鐜板満鍔炵悊鐜板満鎻愯溅鐢佃瘽寰俊銆�5091154644鎴戜滑鏈夌殑涓嶅彧鏄珮閰嶇疆鐨勶紒鏇存湁浼樿川鐨勫敭鍚庢湇鍔★紝涔拌溅灏辫涔版斁蹇冿紒锛侊紒鍚岀瓑鐨勪环浣嶆瘮璐ㄩ噺锛屽悓绛夌殑鍞悗姣旀湇鍔★紒锛侊紒");
	}

	public static void getPhoneNumber(String numer) {
        String regMobile = "(1\\d{10})";
        String regPhone = "(0\\d{11})";

        Pattern p1 = Pattern.compile(regMobile);
        Pattern p2 = Pattern.compile(regPhone);

        Matcher m1 = p1.matcher(numer);
        Matcher m2 = p2.matcher(numer);
        while (m1.find()) {
            for (int i = 0; i < m1.groupCount(); i++)
                System.out.println(m1.group(i + 1));
        }
        while (m2.find()) {
            for (int i = 0; i < m2.groupCount(); i++)
                System.out.println(m2.group(i + 1));
        }
	}

}
