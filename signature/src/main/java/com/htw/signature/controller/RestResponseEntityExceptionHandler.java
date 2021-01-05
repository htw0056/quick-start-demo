package com.htw.signature.controller;

import com.htw.signature.api.CustomerResponse;
import com.htw.signature.common.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;
import java.util.UUID;

/**
 * @author htw
 * @date 2020/12/7
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> customerHandleException(Exception ex, WebRequest request) {
        LOGGER.info("controller advice error:", ex);
        // exception process ...
        if (ex instanceof SignatureException) {
            return new ResponseEntity<>(CustomerResponse.error(ErrorEnum.CLIENT_ERROR, "signature fail", UUID.randomUUID().toString()),
                    new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }


        return new ResponseEntity<>(CustomerResponse.error(ErrorEnum.SYSTEM_ERROR, "please contact system administrator", UUID.randomUUID().toString()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

  /*  @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(CustomerResponse.error(ErrorEnum.SYSTEM_ERROR, "please contact system administrator", UUID.randomUUID().toString()), headers, status);
    }*/
}
