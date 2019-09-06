package com.example.demo.common.filter;

import com.example.demo.common.exception.CaptchaException;
import com.google.code.kaptcha.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/6 17:27
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    public LoginAuthenticationFilter() {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/login", "POST");
        this.setRequiresAuthenticationRequestMatcher(requestMatcher);
        this.setAuthenticationManager(getAuthenticationManager());
//        this.setAuthenticationFailureHandler(new LoginAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String verification = request.getParameter("code");
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);

        if (!captcha.contentEquals(verification)) {
//            throw new CaptchaException("captcha code not matched!");
        }
        return super.attemptAuthentication(request, response);
    }
}
