package com.htw.cryptography.base64;


import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author htw
 * @date 2020/5/2
 */
public class BouncyBase64 {
    public static byte[] encode(String content) {
        return Base64.encode(content.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(String content) {
        return Base64.decode(content.getBytes(StandardCharsets.UTF_8));
    }
}
