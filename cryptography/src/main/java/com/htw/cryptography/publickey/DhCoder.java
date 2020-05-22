package com.htw.cryptography.publickey;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author htw
 * @date 2020/5/2
 */
public class DhCoder {
    public static final String KEY_ALGORITHM = "DH";
    public static final String SECRET_ALGORITHM = "AES";
    private static final int KEY_SIZE = 512;


    public static KeyPair initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    public static KeyPair initKey(byte[] key) throws Exception {
        // 复原公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 根据对方公钥构建我方秘钥对
        DHParameterSpec params = ((DHPublicKey) publicKey).getParams();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(publicKey.getAlgorithm());
        keyPairGenerator.initialize(params);
        return keyPairGenerator.generateKeyPair();
    }


    public static byte[] getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
        // 复原公钥
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);

        // 复原私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // 实例化
        KeyAgreement keyAgreement = KeyAgreement.getInstance(keyFactory.getAlgorithm());
        keyAgreement.init(privateKey1);
        keyAgreement.doPhase(publicKey1, true);
        // 生成本地秘钥
        SecretKey secretKey = keyAgreement.generateSecret(SECRET_ALGORITHM);
        return secretKey.getEncoded();

    }


}
