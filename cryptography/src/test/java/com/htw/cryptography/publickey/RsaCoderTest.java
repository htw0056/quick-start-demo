package com.htw.cryptography.publickey;

import junit.framework.TestCase;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by htw on 2020/5/2.
 */
public class RsaCoderTest extends TestCase {

    public void test() throws Exception {
        KeyPair keyPair = RsaCoder.initKey();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        // 公钥加密
        byte[] bytes = RsaCoder.encryptByPublicKey(data, publicKey.getEncoded());
        // 私钥解密
        byte[] bytes1 = RsaCoder.decryptByPrivateKey(bytes, privateKey.getEncoded());
        System.out.println(new String(bytes1, StandardCharsets.UTF_8));
        Assert.assertArrayEquals(data, bytes1);

        // 私钥加密
        byte[] bytes2 = RsaCoder.encryptByPrivateKey(data, privateKey.getEncoded());
        // 公钥加密
        byte[] bytes3 = RsaCoder.decryptByPublicKey(bytes2, publicKey.getEncoded());
        System.out.println(new String(bytes3, StandardCharsets.UTF_8));
        Assert.assertArrayEquals(data, bytes3);

    }
}