package com.htw.cryptography.symmetric;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * @author htw
 * @date 2020/5/2
 */
public class PbeCoder {
    public static final String ALGORITHM = "PBEWITHMD5andDES";

    public static final int ITERATION_COUNT = 100;

    public static byte[] initSalt() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.generateSeed(8);
    }


    /**
     * @param password
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static Key toKey(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(keySpec);
    }


    public static byte[] encrypt(byte[] data, String password, byte[] salt) throws Exception {
        Key k = toKey(password);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k, pbeParameterSpec);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, String password, byte[] salt) throws Exception{
        Key k = toKey(password);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k, pbeParameterSpec);
        return cipher.doFinal(data);
    }


}
