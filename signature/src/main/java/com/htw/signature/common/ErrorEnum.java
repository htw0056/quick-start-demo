package com.htw.signature.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author htw
 * @date 2020/12/9
 */
@AllArgsConstructor
@Getter
public enum ErrorEnum {

    /**
     * 一切ok
     */
    OK("00000", "ok"),
    /**
     * 用户端错误
     */
    CLIENT_ERROR("A0001", "用户端错误"),
    /**
     * 系统执行出错
     */
    SYSTEM_ERROR("B0001", "系统执行出错"),
    /**
     * 调用第三方服务出错
     */
    MW_ERROR("C0001", "调用第三方服务出错");

    private final String errorCode;
    private final String errorMessage;

}
