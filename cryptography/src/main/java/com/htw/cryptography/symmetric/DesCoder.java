package com.htw.cryptography.symmetric;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author htw
 * @date 2020/5/2
 */
public class DesCoder {
    public static final String KEY_ALGORITHM = "des";
    private static final String CIPHER_ALGORITHM = "des/ecb/pkcs5padding";

    /**
     * 随机生成一个秘钥
     * des算法秘钥长度:56
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] initKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(56);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 根据字节数组生成秘钥
     *
     * @param key
     * @return
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static Key toKey(byte[] key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generateSecret(desKeySpec);
    }


    public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }


}
