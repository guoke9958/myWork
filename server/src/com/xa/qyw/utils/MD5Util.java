package com.xa.qyw.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @description md5加密
 * @author mengp
 * @date 2014-2-12 下午2:45:10 
 */
public class MD5Util {

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) {
		if (b == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String encryptMD5(String s) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	 public static String MD5Encode(String origin, String charsetname) {
	      String resultString = null;
	      try {
	         resultString = new String(origin);
	         MessageDigest md = MessageDigest.getInstance("MD5");
	         if (charsetname == null || "".equals(charsetname))
	            resultString = byteArrayToHexString(md.digest(resultString
	                  .getBytes()));
	         else
	            resultString = byteArrayToHexString(md.digest(resultString
	                  .getBytes(charsetname)));
	      } catch (Exception exception) {
	      }
	      return resultString;
	   }
	 
	 private static String byteArrayToHexString(byte b[]) {
	      StringBuffer resultSb = new StringBuffer();
	      for (int i = 0; i < b.length; i++)
	         resultSb.append(byteToHexString(b[i]));

	      return resultSb.toString();
	   }

	   private static String byteToHexString(byte b) {
	      int n = b;
	      if (n < 0)
	         n += 256;
	      int d1 = n / 16;
	      int d2 = n % 16;
	      return hexDigits[d1] + hexDigits[d2];
	   }
	   
	   private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
	         "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
