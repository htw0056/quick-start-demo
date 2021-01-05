package com.htw.signature.api;

import com.htw.signature.common.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author htw
 * @date 2020/12/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse<T> {
    private T data;
    private String errorCode;
    private String errorMessage;
    private String userTip;
    private String requestId;

    public static <T> CustomerResponse<T> error(ErrorEnum errorEnum, String userTip, String requestId) {
        return new CustomerResponse<>(null, errorEnum.getErrorCode(), errorEnum.getErrorMessage(), userTip, requestId);
    }
}
