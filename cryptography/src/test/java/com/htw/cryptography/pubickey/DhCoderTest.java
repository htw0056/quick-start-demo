package com.htw.cryptography.pubickey;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by htw on 2020/5/2.
 */
public class DhCoderTest extends TestCase {

    public void test() throws Exception {
        // 生成甲方秘钥对
        KeyPair keyPair = DhCoder.initKey();
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();
        System.out.println("甲方公钥: " + Base64.encodeBase64String(aPublic.getEncoded()));
        System.out.println("甲方私钥: " + Base64.encodeBase64String(aPrivate.getEncoded()));

        // 根据甲方公钥生成乙方秘钥对
        KeyPair keyPair1 = DhCoder.initKey(aPublic.getEncoded());
        PublicKey bPublic = keyPair1.getPublic();
        PrivateKey bPrivate = keyPair1.getPrivate();
        System.out.println("乙方公钥: " + Base64.encodeBase64String(bPublic.getEncoded()));
        System.out.println("乙方私钥: " + Base64.encodeBase64String(bPrivate.getEncoded()));

        // 生成甲方本地秘钥(加密秘钥)
        byte[] secretKey1 = DhCoder.getSecretKey(aPublic.getEncoded(), bPrivate.getEncoded());

        // 生成甲方本地秘钥(加密秘钥)
        byte[] secretKey2 = DhCoder.getSecretKey(bPublic.getEncoded(), aPrivate.getEncoded());

        System.out.println("甲方本地秘钥: "+Base64.encodeBase64String(secretKey1));
        System.out.println("乙方本地秘钥: "+Base64.encodeBase64String(secretKey2));

        Assert.assertArrayEquals(secretKey1,secretKey2);
    }
}