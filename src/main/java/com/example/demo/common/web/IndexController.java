package com.example.demo.common.web;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.service.UserService;
import com.example.demo.account.service.impl.RoleServiceImpl;
import com.example.demo.account.service.impl.UserServiceImpl;
import com.example.demo.common.utils.SpringSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/index")
    public String index() {

        return "index";
    }
    @RequestMapping("/toChooseRole")
    public String toChooseRole(ModelMap map) {
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
//        userService.findUserRolesById(currentUserDetails.getId());
        Set<SysRole> sysRoles = currentUserDetails.getSysRoles();
        map.addAttribute("roleList", sysRoles);
        return "chooseRole";
    }
    @PostMapping("/chooseRole/{roleId}")
    public String toChooseRole(@PathVariable("roleId") Long roleId) {
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
        Long id = userService.findUserRoleIdByRoleId(currentUserDetails.getId(), roleId);
        Set<SysRole> sysRoles = currentUserDetails.getSysRoles();
        map.addAttribute("roleList", sysRoles);
        return "chooseRole";
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

//    @RequestMapping("/getRes")
//    public String getRes() {
//
//    }
}
