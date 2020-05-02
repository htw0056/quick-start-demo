package com.htw.cryptography.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author htw
 * @date 2020/5/2
 */
public class NativeMessageDigest {

    public static byte[] md5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(data);
    }
}
