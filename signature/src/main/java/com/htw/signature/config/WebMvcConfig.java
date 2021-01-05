package com.htw.signature.config;

import com.htw.signature.interceptor.SignatureInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author htw
 * @date 2020/12/8
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public SignatureInterceptor signatureInterceptor() {
        return new SignatureInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signatureInterceptor()).addPathPatterns("/test/**");
    }

}
