package com.xa.qyw.otherweb.note;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * 27 md5加密产生，产�?28位（bit）的mac 28 �?28bit Mac转换�?6进制代码 29
	 * 
	 * @param strSrc
	 *            30
	 * @param key
	 *            31
	 * @return 32
	 */
	public static String MD5Encode(String strSrc) {
		return MD5Encode(strSrc, "");
	}

	/**
	 * 27 md5加密产生，产�?28位（bit）的mac 28 �?28bit Mac转换�?6进制代码 29
	 * 
	 * @param strSrc
	 *            30
	 * @param key
	 *            31
	 * @return 32
	 */
	public static String MD5Encode(String strSrc, String key) {

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes("UTF8"));
			StringBuilder result = new StringBuilder(32);
			byte[] temp;
			temp = md5.digest(key.getBytes("UTF8"));
			for (int i = 0; i < temp.length; i++) {
				result.append(Integer.toHexString(
						(0x000000ff & temp[i]) | 0xffffff00).substring(6));
			}
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public static void main(String[] args) throws Exception {
		String aaa = "123456";
		String mac128byte = MD5Encode(aaa, "");
		System.out.println("md5加密结果32 bit------------->:" + mac128byte);
	}

}
