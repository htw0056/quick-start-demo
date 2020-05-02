package com.htw.cryptography.digest;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author htw
 * @date 2020/5/2
 */
public class ApacheMessageDigest {

    public static byte[] md5(byte[] data) {
        return DigestUtils.md5(data);
    }

    public static byte[] sha256(byte[] data) {
        return DigestUtils.sha256(data);
    }
}
