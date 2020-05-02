package com.htw.cryptography.base64;

import junit.framework.TestCase;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;

/**
 * Created by htw on 2020/5/2.
 */
public class Base64Test extends TestCase {

    public void test() {
        String s = "你好!Hello,world!";
        byte[] encode = ApacheBase64.encode(s);
        String encodeString = new String(encode, StandardCharsets.UTF_8);
        System.out.println(s + " -> base64 result: " + encodeString);

        byte[] decode = ApacheBase64.decode(encodeString);
        String decodeString = new String(decode, StandardCharsets.UTF_8);

        assertEquals(s, decodeString);
    }

    public void testBouncy() {
        String s = "你好!Hello,world!";
        byte[] encode = BouncyBase64.encode(s);
        String encodeString = new String(encode, StandardCharsets.UTF_8);
        System.out.println(s + " -> base64 result: " + encodeString);

        byte[] decode = BouncyBase64.decode(encodeString);
        String decodeString = new String(decode, StandardCharsets.UTF_8);

        assertEquals(s, decodeString);
    }
}