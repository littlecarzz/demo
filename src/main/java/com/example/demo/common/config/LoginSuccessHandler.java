package com.example.demo.common.config;

import com.example.demo.account.entity.Dljl;
import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.service.impl.DljlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * 登录成功后的处理
 * 可实现主页跳转；
 * 用户查询，存储等
 * @author littlecar
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private DljlServiceImpl dljlService;
	@Override  
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
        //获得授权后可得到用户信息
        SecurityUser userDetails = (SecurityUser)authentication.getPrincipal();
        //输出登录提示信息
        System.out.println("欢迎 " + userDetails.getUsername() + " 登录");
        System.out.println("IP :"+getIpAddress(request));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //保存登录记录
        Dljl dljl = new Dljl(userDetails.getUsername(),df.format(new Date()),getIpAddress(request));
        dljlService.save(dljl);
        Integer isTop = userDetails.getIsTop();
        if (isTop==0){
            response.sendRedirect(request.getContextPath()+"/index");
        } else if (isTop == 1) {
            request.getRequestDispatcher("/toChooseRole").forward(request,response);
//            response.sendRedirect(request.getContextPath()+"/toChooseRole");
        } else {

        }

    }  
    
    public String getIpAddress(HttpServletRequest request){    
        String ip = request.getHeader("x-forwarded-for");    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("Proxy-Client-IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("WL-Proxy-Client-IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_CLIENT_IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getRemoteAddr();    
        }    
        return ip;    
    }  
}