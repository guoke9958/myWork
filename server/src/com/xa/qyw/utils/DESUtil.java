package com.xa.qyw.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * MDES鍔犲瘑绫�
 *
 * @author 寮犲
 */
public final class DESUtil {

    /**
     * 绂佹鐢╪ew鏂瑰紡鍒涘缓瀹炰緥
     */
    private DESUtil() {
    }

    /**
     * 瀛楃涓插父閲�
     */
    private final static String DES = "DES";

    /**
     * Description 鏍规嵁閿�杩涜鍔犲瘑
     *
     * @param data
     * @param key  鍔犲瘑閿産yte鏁扮粍
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        return new BASE64Encoder().encode(bt);
    }

    /**
     * Description 鏍规嵁閿�杩涜瑙ｅ瘑
     *
     * @param data
     * @param key  鍔犲瘑閿産yte鏁扮粍
     * @return
     * @throws java.io.IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * Description 鏍规嵁閿�杩涜鍔犲瘑
     *
     * @param data
     * @param key  鍔犲瘑閿産yte鏁扮粍
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 鐢熸垚涓�釜鍙俊浠荤殑闅忔満鏁版簮
        SecureRandom sr = new SecureRandom();
        // 浠庡師濮嬪瘑閽ユ暟鎹垱寤篋ESKeySpec瀵硅薄
        DESKeySpec dks = new DESKeySpec(key);
        // 鍒涘缓涓�釜瀵嗛挜宸ュ巶锛岀劧鍚庣敤瀹冩妸DESKeySpec杞崲鎴怱ecretKey瀵硅薄
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher瀵硅薄瀹為檯瀹屾垚鍔犲瘑鎿嶄綔
        Cipher cipher = Cipher.getInstance(DES);
        // 鐢ㄥ瘑閽ュ垵濮嬪寲Cipher瀵硅薄
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }


    /**
     * Description 鏍规嵁閿�杩涜瑙ｅ瘑
     *
     * @param data
     * @param key  鍔犲瘑閿産yte鏁扮粍
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 鐢熸垚涓�釜鍙俊浠荤殑闅忔満鏁版簮
        SecureRandom sr = new SecureRandom();
        // 浠庡師濮嬪瘑閽ユ暟鎹垱寤篋ESKeySpec瀵硅薄
        DESKeySpec dks = new DESKeySpec(key);
        // 鍒涘缓涓�釜瀵嗛挜宸ュ巶锛岀劧鍚庣敤瀹冩妸DESKeySpec杞崲鎴怱ecretKey瀵硅薄
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher瀵硅薄瀹為檯瀹屾垚瑙ｅ瘑鎿嶄綔
        Cipher cipher = Cipher.getInstance(DES);
        // 鐢ㄥ瘑閽ュ垵濮嬪寲Cipher瀵硅薄
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
}
