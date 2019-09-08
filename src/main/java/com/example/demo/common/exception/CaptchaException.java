package com.example.demo.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 描述：处理验证码异常
 * @author littlecar
 * @date 2019/9/6 17:29
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
