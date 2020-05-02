package com.htw.cryptography.base64;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @author htw
 * @date 2020/5/2
 */
public class ApacheBase64 {

    public static byte[] encode(String content) {
        return Base64.encodeBase64(content.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(String content) {
        return Base64.decodeBase64(content.getBytes(StandardCharsets.UTF_8));
    }
}
