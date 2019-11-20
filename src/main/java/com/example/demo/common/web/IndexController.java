package com.example.demo.common.web;

import com.example.demo.account.entity.ResourceInfo;
import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysResource;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.service.ResourceRoleService;
import com.example.demo.account.service.UserService;
import com.example.demo.account.service.impl.ResourceRoleServiceImpl;
import com.example.demo.account.service.impl.ResourceServiceImpl;
import com.example.demo.account.service.impl.RoleServiceImpl;
import com.example.demo.account.service.impl.UserServiceImpl;
import com.example.demo.common.utils.SpringSecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
@Api(value = "/indexOfmain",description = "Operations about index")
public class IndexController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ResourceServiceImpl resourceService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("/toChooseRole")
    @ApiOperation(value = "/toChooseRole")
    public String toChooseRole(ModelMap map,HttpServletRequest request) {
        String password = request.getParameter("password");
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
        currentUserDetails.setUserPwd(password);
        Set<SysRole> sysRoles = currentUserDetails.getSysRoles();
        map.addAttribute("roleList", sysRoles);
        return "chooseRole";
    }
    @GetMapping("/chooseRole/{roleId}")
    public String toChooseRole(@PathVariable("roleId") Long roleId,HttpServletRequest request) {
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
        currentUserDetails.setCurrUserRoleId(roleId);
        userService.doRoleChoose(roleId,request,currentUserDetails);
        return "redirect:/index";
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

    @RequestMapping("/getRes")
    @ResponseBody
    public Map<String, List<ResourceInfo>> getRes(HttpServletRequest request) {
        Map<String, List<ResourceInfo>> map = new HashMap<>();
        Long userRoleId = SpringSecurityUtils.getCurrentUserRoleId();
        List<SysResource> resourceList=resourceService.findResourceByRoleId(userRoleId);
        List<ResourceInfo> resourceInfos = resourceService.combineJson(resourceList);
        map.put("contentManagement",resourceInfos);
        return map;
    }
}
