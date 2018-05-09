package com.yw.platform.tools;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {
    /**
     * 生成经BASE64编码后的RSA公钥和私钥
     */
    public static void createKeyPairs() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024, new SecureRandom());
            KeyPair pair = generator.generateKeyPair();
            PublicKey pubKey = pair.getPublic();
            PrivateKey privKey = pair.getPrivate();
            byte[] pubk = pubKey.getEncoded();
            byte[] privk = privKey.getEncoded();
            // base64编码，屏蔽特殊字符
            String strpk = new String(Base64.encode(pubk,Base64.DEFAULT));
            String strprivk = new String(Base64.encode(privk,Base64.DEFAULT));
            Log.e("strpk", strpk);
            Log.e("strprivk", strprivk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * RSA公钥加密
     * @param content   待加密的明文
     * @param pubKey    RSA公钥
     * @return  经BASE64编码后的密文
     */
    public static String pubKeyEnc(String content,String pubKey){
        try {
            KeyFactory keyf = KeyFactory.getInstance("RSA","BC");
            //获取公钥
            InputStream is = new ByteArrayInputStream(pubKey.getBytes("utf-8"));
            byte[] pubbytes = new byte[new Long(pubKey.length()).intValue()];
            is.read(pubbytes);
            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(pubbytes,Base64.DEFAULT));
            PublicKey pkey = keyf.generatePublic(pubX509);

            //公钥加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pkey);
            byte[] cipherText = cipher.doFinal(content.getBytes());
            // 将加密结果转换为Base64编码结果；便于internet传送
            return Base64.encodeToString(cipherText,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * RSA公钥解密
     * @param ciphertext 经BASE64编码过的待解密的密文
     * @param pubKey RSA公钥
     * @return utf-8编码的明文
     */
    public static String pubKeyDec(String ciphertext ,String pubKey){
        try {
            KeyFactory keyf = KeyFactory.getInstance("RSA","BC");

            //获取公钥
            InputStream is = new ByteArrayInputStream(pubKey.getBytes("utf-8"));
            byte[] pubbytes = new byte[new Long(pubKey.length()).intValue()];
            is.read(pubbytes);
            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(pubbytes,Base64.DEFAULT));
            PublicKey pkey = keyf.generatePublic(pubX509);

            //公钥解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pkey);
            byte[] text = cipher.doFinal(Base64.decode(ciphertext,Base64.DEFAULT));

            return new String(text,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * RSA私钥加密
     * @param content 待加密的明文
     * @param privKey RSA私钥
     * @return  经BASE64编码后的密文
     */
    public static String privKeyEnc(String content,String privKey){
        try {
            KeyFactory keyf = KeyFactory.getInstance("RSA","BC");

            //获取私钥
            InputStream key = new ByteArrayInputStream(privKey.getBytes("utf-8"));
            byte[] pribytes = new byte[new Long(privKey.length()).intValue()];
            key.read(pribytes);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(pribytes,Base64.DEFAULT));
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);

            //私钥加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, prikey);
            byte[] cipherText = cipher.doFinal(content.getBytes());

            //将加密结果转换为Base64编码结果；便于internet传送
            return Base64.encodeToString(cipherText,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * RSA私钥解密
     * @param ciphertext    经BASE84编码过的待解密密文
     * @param privKey   RSA私钥
     * @return  utf-8编码的明文
     */
    public static String privKeyDec(String ciphertext ,String privKey){
        try {
            KeyFactory keyf = KeyFactory.getInstance("RSA","BC");
//          获取私钥
            InputStream key = new ByteArrayInputStream(privKey.getBytes("utf-8"));
            byte[] pribytes = new byte[new Long(privKey.length()).intValue()];
            key.read(pribytes);
            byte[] buffer = Base64.decode(pribytes,Base64.DEFAULT);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(buffer);
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);

            //私钥解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, prikey);
            byte[] text=Base64.decode(ciphertext,Base64.DEFAULT);
            byte[] content = cipher.doFinal(text);
            return new String(content,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA私钥数字签名
     * @param content 待签内容
     * @param privKey RSA私钥
     * @return 经BASE64编码后的签名串
     */
    public static String sign(String content,String privKey){
        try {
            KeyFactory keyf=KeyFactory.getInstance("RSA","BC");

            //获取私钥
            InputStream key = new ByteArrayInputStream(privKey.getBytes("utf-8"));
            byte[] pribytes = new byte[new Long(privKey.length()).intValue()];
            key.read(pribytes);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(new String(pribytes),Base64.DEFAULT));
            PrivateKey priKey=keyf.generatePrivate(priPKCS8);

            //实例化Signature；签名算法：MD5withRSA
            Signature signature = Signature.getInstance("MD5withRSA");
            //初始化Signature
            signature.initSign(priKey);
            //更新
            signature.update(content.getBytes());
            return Base64.encodeToString(signature.sign(),Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA公钥校验数字签名
     * @param content 待校验的内容
     * @param pubKey RSA公钥
     * @param signedStr 签名字符串
     * @return  true:校验成功；false:校验失败
     */
    public static boolean verify(String content,String pubKey,String signedStr){
        try {
            //实例化密钥工厂
            KeyFactory keyf=KeyFactory.getInstance("RSA","BC");

            //获取公钥
            InputStream is = new ByteArrayInputStream(pubKey.getBytes("utf-8"));
            byte[] pubbytes = new byte[new Long(pubKey.length()).intValue()];
            is.read(pubbytes);
            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(new String(pubbytes),Base64.DEFAULT));
            PublicKey pkey = keyf.generatePublic(pubX509);

            //实例化Signature；签名算法：MD5withRSA
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(pkey);
            signature.update(content.getBytes());
            //验证
            return signature.verify(Base64.decode(signedStr,Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}