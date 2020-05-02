package com.htw.cryptography.hmac;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.Mac;
import java.security.InvalidKeyException;

/**
 * @author htw
 * @date 2020/5/2
 */
public class ApacheHmac {

    public static byte[] encodeHmacMd5(byte[] data, byte[] key) throws InvalidKeyException {
        Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_MD5, key);
        HmacUtils.updateHmac(mac, data);
        return mac.doFinal();
    }

}
