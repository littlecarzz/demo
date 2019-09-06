package com.example.demo.common.web;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/4 14:50
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/login-error")
    public String loginError(HttpServletRequest request) {
        AuthenticationException exp = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exp instanceof BadCredentialsException) {
            request.setAttribute("error_msg", "用户名或密码错误");
        }else if (exp instanceof AccountExpiredException) {
            request.setAttribute("error_msg", "用户已过期");
        } else if (exp instanceof LockedException) {
            request.setAttribute("error_msg", "用户已被锁定");
        } else {
            request.setAttribute("error_msg", "其他错误");
        }
        return "login";
    }
    @RequestMapping("/403")
    public String error() {
        return "/error/403";
    }
}
