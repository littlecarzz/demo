package com.example.demo.common.web;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysRole;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
/*        SecurityUser userDetail = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<SysRole> sysRoles = userDetail.getSysRoles();
        List<SysResource> resourceList = new LinkedList<>();
        for (SysRole role:
             sysRoles) {

        }*/
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/403")
    public String error() {
        return "error/403";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @RequestMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/main")
    public String main(HttpServletResponse response) {
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "main";
    }
}
