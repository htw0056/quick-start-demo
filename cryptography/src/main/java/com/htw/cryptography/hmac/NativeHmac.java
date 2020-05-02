package com.htw.cryptography.hmac;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author htw
 * @date 2020/5/2
 */
public class NativeHmac {

    /**
     * 随机初始化一个key,对于Hmac来说，key的长度没限制
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] initKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacMd5(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(secretKey);
        return mac.doFinal(data);
    }


}
