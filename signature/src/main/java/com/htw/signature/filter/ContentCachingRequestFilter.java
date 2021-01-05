package com.htw.signature.filter;

import com.htw.signature.servlet.CachedBodyHttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 对ServletRequest 包装,使其支持body可重读
 * 缺点: 耗内存
 * link: https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
 *
 * @author htw
 * @date 2020/12/2
 */
@Component
@Order(1)
public class ContentCachingRequestFilter implements Filter {
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 不支持application/x-www-form-urlencoded,原因:spring解析该post方式时，会提前调用getParameterMap()并读取body的内容，导致验签时body获取为空，验证失败
        String contentType = httpServletRequest.getContentType();
        String method = httpServletRequest.getMethod();
        if (contentType != null && contentType.contains(FORM_CONTENT_TYPE) &&
                HttpMethod.POST.matches(method)) {
            throw new RuntimeException(FORM_CONTENT_TYPE + " is not support");
        }
        chain.doFilter(new CachedBodyHttpServletRequest((HttpServletRequest) request), response);
    }
}
