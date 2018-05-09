package com.yw.platform.tools;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cxb on 2018/3/27.
 */

public class AESUtil {
    /**
     * 生成AES密钥
     * @param strkey
     * @return
     * @throws Exception
     */
    public static String createKeyPairs(String strkey) throws  Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT >= 17){
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        }else{
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(strkey.getBytes("UTF-8"));
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        Log.e("AES----KEY",new String(raw,"UTF-8"));
        return new String(raw);
    }
    /**
     * AES加密，传入需要加密的明文和key
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String src) throws Exception {
        byte[] result = encrypt(key.getBytes("UTF-8"), src.getBytes("UTF-8"));
        return Base64.encodeToString(result, Base64.DEFAULT);
    }
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    /**
     * AES解密，传入密文和对应的key
     * @param key
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decrypt(String key, String encrypted) throws Exception {
        byte[] result = decrypt(key.getBytes(), Base64.decode(encrypted, Base64.DEFAULT));
        return new String(result,"UTF-8");
    }
    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
