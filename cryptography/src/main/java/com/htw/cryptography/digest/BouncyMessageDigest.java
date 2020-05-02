package com.htw.cryptography.digest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * @author htw
 * @date 2020/5/2
 */
public class BouncyMessageDigest {

    public static byte[] sha224(byte[] data) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest instance = MessageDigest.getInstance("SHA-224");
        return instance.digest(data);
    }
}
