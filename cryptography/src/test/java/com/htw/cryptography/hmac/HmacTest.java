package com.htw.cryptography.hmac;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by htw on 2020/5/2.
 */
public class HmacTest extends TestCase {

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

    public void testHmacMd5() throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] key = initKey();
        byte[] result = NativeHmac.encodeHmacMd5(data, key);
        byte[] result2 = ApacheHmac.encodeHmacMd5(data, key);
        System.out.println(Hex.encodeHexString(result));
        Assert.assertArrayEquals(result, result2);
    }

    public void testHmacMd2() throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] key = initKey();
        byte[] result = BouncyHmac.encodeHmacMd2(data, key);
        System.out.println(Hex.encodeHexString(result));

    }

}