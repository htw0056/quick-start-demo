package com.htw.cryptography.sign;

import org.bouncycastle.asn1.cmp.PKIFailureInfo;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author htw
 * @date 2020/5/2
 */
public class RsaSign {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    public static final int KEY_SIXE = 512;

    /**
     * 随机生成一组公私钥
     *
     * @return
     * @throws Exception
     */
    public static KeyPair initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIXE);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] sign(byte[] data, byte[] key) throws Exception {
        // 复原私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);

        // 签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verify(byte[] data, byte[] key, byte[] sign) throws Exception {
        // 复原公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);

        // 验签
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }
}
