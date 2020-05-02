package com.htw.cryptography.pubickey;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author htw
 * @date 2020/5/2
 */
public class RsaCoder {
    public static final String KEY_ALGORITHM = "RSA";
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

    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 复原私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);

        // 解密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 复原公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);

        // 解密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 复原私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);

        // 加密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 复原公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);

        // 加密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }
}
