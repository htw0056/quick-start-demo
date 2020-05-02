package com.htw.cryptography.sign;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by htw on 2020/5/2.
 */
public class RsaSignTest extends TestCase {

    public void test() throws Exception {
        KeyPair keyPair = RsaSign.initKey();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] sign = RsaSign.sign(data, privateKey.getEncoded());
        System.out.println("sign: "+ Hex.encodeHexString(sign));
        boolean verify = RsaSign.verify(data, publicKey.getEncoded(), sign);
        assertTrue(verify);
    }
}