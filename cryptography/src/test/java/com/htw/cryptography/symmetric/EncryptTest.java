package com.htw.cryptography.symmetric;

import junit.framework.TestCase;
import org.junit.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by htw on 2020/5/2.
 */
public class EncryptTest extends TestCase {

    public void testEncrypt() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        byte[] key = DesCoder.initKey();
        byte[] encrypt = DesCoder.encrypt(data, key);
        byte[] decrypt = DesCoder.decrypt(encrypt, key);
        Assert.assertArrayEquals(decrypt, data);
    }

    public void testPbeCoder() throws Exception {
        byte[] data = "你好!Hello,world!".getBytes(StandardCharsets.UTF_8);
        String password = "htw";
        byte[] salt = PbeCoder.initSalt();
        byte[] encrypt = PbeCoder.encrypt(data, password, salt);
        byte[] decrypt = PbeCoder.decrypt(encrypt, password, salt);
        Assert.assertArrayEquals(decrypt, data);
    }
}