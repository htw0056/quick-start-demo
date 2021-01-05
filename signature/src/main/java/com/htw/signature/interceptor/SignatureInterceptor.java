package com.htw.signature.interceptor;

import com.htw.signature.common.NotationConstants;
import com.htw.signature.utils.SignatureUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器处理签名
 *
 * @author htw
 * @date 2020/12/8
 */
public class SignatureInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureInterceptor.class);

    private static final Map<String, String> USER_INFO_MAP = new HashMap<String, String>() {
        {
            put("htw", "abcd123");
            put("admin", "haha");
        }
    };
    private static final Integer REQUEST_EXPIRE_TIME = 5;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        String date = request.getHeader(HttpHeaders.DATE);
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        Map<String, String[]> parameterMap = request.getParameterMap();
        String path = request.getRequestURI();

        // check date format
        SignatureUtil.notNull(date, "Header Date value");
        Date requestDate = DateUtils.parseDate(date, new String[]{DateUtils.PATTERN_RFC1123});
        SignatureUtil.check(null != requestDate, "Header Date value invalid");
        Date now = new Date();
        SignatureUtil.check(now.after(requestDate) && now.before(org.apache.commons.lang3.time.DateUtils.addMinutes(requestDate, REQUEST_EXPIRE_TIME)),
                "request expired");

        // check authorization
        SignatureUtil.notNull(authorization, "Header Authorization value");
        String[] split = StringUtils.split(authorization, NotationConstants.COLON);
        SignatureUtil.check(split != null && split.length == 2, "Authorization value invalid");
        String accessKeyId = split[0];
        String signature = split[1];
        SignatureUtil.check(USER_INFO_MAP.containsKey(accessKeyId), "Authorization value invalid");
        String accessKeySecret = USER_INFO_MAP.get(accessKeyId);

        // format parameter
        Map<String, String> formatParameterMap = new HashMap<>(4);
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            formatParameterMap.put(entry.getKey(), String.join(NotationConstants.COMMA, entry.getValue()));
        }

        boolean verifySignature = SignatureUtil.verifySignature(signature, method, path, formatParameterMap,
                StreamUtils.copyToByteArray(request.getInputStream()),
                contentType, date, accessKeySecret);
        SignatureUtil.check(verifySignature, "signature verify failed");

        return true;
    }


}
