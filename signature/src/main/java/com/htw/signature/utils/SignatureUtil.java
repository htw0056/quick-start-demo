package com.htw.signature.utils;

import com.htw.signature.common.NotationConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 签名工具类
 *
 * @author htw
 * @date 2020/12/2
 */
public class SignatureUtil {

    public static String signatureAppendUserInfo(String verb, String path, Map<String, String> params,
                                                 byte[] body, String contentType, String date,
                                                 String accessKeyId, String accessKeySecret) {
        return accessKeyId + NotationConstants.COLON + signature(verb, path, params, body, contentType, date, accessKeySecret);
    }


    public static boolean verifySignature(String signature, String verb, String path, Map<String, String> params,
                                          byte[] body, String contentType, String date, String accessKeySecret) {
        return Objects.equals(signature, signature(verb, path, params, body, contentType, date, accessKeySecret));
    }


    public static String signature(String verb, String path, Map<String, String> params,
                                   byte[] body, String contentType, String date, String accessKeySecret) {
        boolean hasBody = true;
        if (HttpGet.METHOD_NAME.equalsIgnoreCase(verb)) {
            hasBody = false;
        }

        String canonicalizedResource = SignatureUtil.canonicalizedResource(path, params);
        String signString = verb + NotationConstants.LF
                + (hasBody ? DigestUtils.md5Hex(body) : NotationConstants.EMPTY) + NotationConstants.LF
                + (hasBody ? contentType : NotationConstants.EMPTY) + NotationConstants.LF
                + date + NotationConstants.LF
                + canonicalizedResource;
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, accessKeySecret.getBytes(StandardCharsets.UTF_8));
        byte[] hmac = hmacUtils.hmac(signString.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(hmac);
    }

    /**
     * 规范化资源
     *
     * @param path   路径
     * @param params 参数
     * @return 规范化后资源字符串
     */
    public static String canonicalizedResource(String path, Map<String, String> params) {

        TreeMap<String, String> sortedMap = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        boolean hasParam = false;
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 非空的key和value参与签名
            if (StringUtils.isNoneEmpty(key, value)) {
                if (hasParam) {
                    sb.append(NotationConstants.AMPERSAND);
                } else {
                    hasParam = true;
                }
                sb.append(key).append(NotationConstants.EQUAL).append(value);
            }
        }
        String paramString = sb.toString();

        if (StringUtils.isBlank(paramString)) {
            return path;
        } else {
            return path + NotationConstants.QUESTION_MARK + paramString;
        }
    }

    public static void check(boolean expression, String message) throws SignatureException {
        if (!expression) {
            throw new SignatureException(message);
        }
    }

    public static <T> T notNull(T argument, String name) throws SignatureException {
        if (argument == null) {
            throw new SignatureException(name + " may not be null");
        } else {
            return argument;
        }
    }
}
