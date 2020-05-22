package com.htw.cryptography;

import junit.framework.TestCase;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by htw on 2020/5/22.
 */
public class ReadPemTest extends TestCase {

    /**
     * 利用Bouncy库解析各种PEM
     *
     * @throws IOException
     */
    public void testReadPem() throws IOException {
        /*
        String a = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICXAIBAAKBgQDDybFIGMlXWB9/b0mcQ/Ng5gu6/dFa8gbnbAY89FnHfkxtSgsd\n" +
                "nIGFSu9yuJvcuZYO8wgx1Wp0mwkYpQ2DmR52LvS56egWxaVTrlpJ1Dp3WuRnXWb3\n" +
                "l2pBwM56lpAzgysKqnkAvKD6a9h0DYYljoLWXZJDCIwnyBEiDN9WxEZPfQIDAQAB\n" +
                "AoGAYWoZKyYqB6txAJb+qSGmFrBfZcqkP/vsM3ksIWfyw6+zly0gGrukNg3y1cHE\n" +
                "yy4L26RQj3sAXMRG4lw9PR0SahqhZWO5PQjy9jkYFu8al72b4GGDQSpKEfOCMLJ2\n" +
                "ZxPXfbMLdJC/mnXQdBxML10J5O6gjwbOVPvh6FuZv1Kg5oECQQD+DMoXX4ydSi8W\n" +
                "6vHirJmzseOGyfE3jRlbrmJbWJmAX0oS3L0H29yeSesb4yHobksVJpH8HRSmHQwg\n" +
                "TcOsg2/rAkEAxUpq4h7EbNC5ORzQuiWNkrGmx4LgVTljM3m/43u8GOpDplxkpYm9\n" +
                "KewqIZ5mj0WEjvjmQDUTDWykI1HNiA7MNwJAC5A7dprjxJkG0a05+05N5K75Iz53\n" +
                "20Zx3Alw8qVyvzQXJAqmFAB+5zmIGQnalkDG7ByIUohkUzdJQQMpH+YPpwJBAJ2C\n" +
                "Nibk7Wj6koXYTYPvq0Fsd3xdLoCb2mkkUGBrTtaNYQkWRnwqpU0s8M36SmMj2xrR\n" +
                "9/FW7ikPjVNtYau4NK0CQBD+RrCPCu1wWAL4ZgSlYAVL0T53hVcBEkzF7l/DrPFg\n" +
                "1GccqcGYQ/heWwVuAwJnmNWglTUWXR8IQMeT2pW0+1A=\n" +
                "-----END RSA PRIVATE KEY-----\n";
*/

/*
        String a = "-----BEGIN PRIVATE KEY-----\n" +
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPJsUgYyVdYH39v\n" +
                "SZxD82DmC7r90VryBudsBjz0Wcd+TG1KCx2cgYVK73K4m9y5lg7zCDHVanSbCRil\n" +
                "DYOZHnYu9Lnp6BbFpVOuWknUOnda5GddZveXakHAznqWkDODKwqqeQC8oPpr2HQN\n" +
                "hiWOgtZdkkMIjCfIESIM31bERk99AgMBAAECgYBhahkrJioHq3EAlv6pIaYWsF9l\n" +
                "yqQ/++wzeSwhZ/LDr7OXLSAau6Q2DfLVwcTLLgvbpFCPewBcxEbiXD09HRJqGqFl\n" +
                "Y7k9CPL2ORgW7xqXvZvgYYNBKkoR84IwsnZnE9d9swt0kL+addB0HEwvXQnk7qCP\n" +
                "Bs5U++HoW5m/UqDmgQJBAP4MyhdfjJ1KLxbq8eKsmbOx44bJ8TeNGVuuYltYmYBf\n" +
                "ShLcvQfb3J5J6xvjIehuSxUmkfwdFKYdDCBNw6yDb+sCQQDFSmriHsRs0Lk5HNC6\n" +
                "JY2SsabHguBVOWMzeb/je7wY6kOmXGSlib0p7CohnmaPRYSO+OZANRMNbKQjUc2I\n" +
                "Dsw3AkALkDt2muPEmQbRrTn7Tk3krvkjPnfbRnHcCXDypXK/NBckCqYUAH7nOYgZ\n" +
                "CdqWQMbsHIhSiGRTN0lBAykf5g+nAkEAnYI2JuTtaPqShdhNg++rQWx3fF0ugJva\n" +
                "aSRQYGtO1o1hCRZGfCqlTSzwzfpKYyPbGtH38VbuKQ+NU21hq7g0rQJAEP5GsI8K\n" +
                "7XBYAvhmBKVgBUvRPneFVwESTMXuX8Os8WDUZxypwZhD+F5bBW4DAmeY1aCVNRZd\n" +
                "HwhAx5PalbT7UA==\n" +
                "-----END PRIVATE KEY-----\n";
*/

        String a = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDybFIGMlXWB9/b0mcQ/Ng5gu6\n" +
                "/dFa8gbnbAY89FnHfkxtSgsdnIGFSu9yuJvcuZYO8wgx1Wp0mwkYpQ2DmR52LvS5\n" +
                "6egWxaVTrlpJ1Dp3WuRnXWb3l2pBwM56lpAzgysKqnkAvKD6a9h0DYYljoLWXZJD\n" +
                "CIwnyBEiDN9WxEZPfQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        String b = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDybFIGMlXWB9/b0mcQ/Ng5gu6\n" +
                "/dFa8gbnbAY89FnHfkxtSgsdnIGFSu9yuJvcuZYO8wgx1Wp0mwkYpQ2DmR52LvS5\n" +
                "6egWxaVTrlpJ1Dp3WuRnXWb3l2pBwM56lpAzgysKqnkAvKD6a9h0DYYljoLWXZJD\n" +
                "CIwnyBEiDN9WxEZPfQIDAQAB\n";

        // 1. 利用PemReader
        PemReader pemReader = new PemReader(new InputStreamReader(new ByteArrayInputStream(a.getBytes())));
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();

        // 2. 原始数据反解
        byte[] decode = Base64.decode(b);

        Assert.assertArrayEquals(content, decode);
    }
}
