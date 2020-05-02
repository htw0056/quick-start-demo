package com.htw.cryptography.hmac;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 *
 * @author htw
 * @date 2020/5/2
 */
public class BouncyHmac {

    public static byte[] encodeHmacMd2(byte[] data,byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacMD2");
        Mac mac = Mac.getInstance("HmacMD2");
        mac.init(secretKey);
        return mac.doFinal(data);
    }
}
