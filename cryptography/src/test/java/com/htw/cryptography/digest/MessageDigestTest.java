package com.htw.cryptography.digest;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * Created by htw on 2020/5/2.
 */
public class MessageDigestTest extends TestCase {

    public void testNativeMessageDigest() throws NoSuchAlgorithmException {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] nativeMd5 = NativeMessageDigest.md5(data);
        byte[] apacheMd5 = ApacheMessageDigest.md5(data);
        System.out.println(Hex.encodeHex(nativeMd5));
        System.out.println(Hex.encodeHex(apacheMd5));

        Assert.assertArrayEquals(nativeMd5, apacheMd5);
    }

    public void testApacheMessageDigest() {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = ApacheMessageDigest.sha256(data);
        System.out.println(Hex.encodeHex(bytes));
    }

    public void testBouncyMessageDigest() throws NoSuchAlgorithmException {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = BouncyMessageDigest.sha224(data);
        System.out.println(Hex.encodeHex(bytes));
    }

}