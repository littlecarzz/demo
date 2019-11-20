package com.example.demo.account.web;

import com.example.demo.account.entity.*;
import com.example.demo.account.service.impl.DljlServiceImpl;
import com.example.demo.account.service.impl.RoleServiceImpl;
import com.example.demo.account.service.impl.UserServiceImpl;
import com.example.demo.common.utils.BCryptUtil;
import com.example.demo.common.utils.Constant;
import com.example.demo.common.utils.SpringSecurityUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/12 10:11
 */
@Controller
@Api(value = "/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DljlServiceImpl dljlService;
    @Autowired
    private RoleServiceImpl roleService;


    @RequestMapping("/toUserList")
    public String toUserList() {
        return "user/userList";
    }

    @RequestMapping("/userList")
    @ResponseBody
    public Map<String,Object> userList( String username) {
        Map<String, Object> map = new HashMap<>();
        String roles = "";
        List<UserInfo> userInfoList = new LinkedList <>();
        List<SysUser> userList = new LinkedList<>();
        if (StringUtils.isNotEmpty(username)) {
            username = "%" + username + "%";
            userList = userService.findByUsernameLike(username);
        }else{
            userList = userService.findAll();
        }
        for (SysUser user :
                userList) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setSex(user.getSex());
            userInfo.setEmail(user.getEmail());
            userInfo.setMobile(user.getMobile());
            userInfo.setStatus(user.getStatus());
            userInfo.setLastTime(dljlService.findLastTimeByUsername(user.getUsername()));
            Set<SysRole> sysRoles = user.getSysRoles();
            int i=0;
            for (SysRole role :
                    sysRoles) {
                if(i==sysRoles.size()-1){
                    roles+= Constant.roleMap.get(role.getName());
                }else{
                    roles+=Constant.roleMap.get(role.getName())+"/";
                }
                i++;
            }
            userInfo.setRoles(roles);
            roles="";
            userInfoList.add(userInfo);
        }
        map.put("data", userInfoList);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", userList.size());
        return map;
    }

    @RequestMapping("/toUserAdd")
    public String toUserAdd() {
        return "user/userAdd";
    }

    @RequestMapping("/userAdd")
    @ResponseBody
    public String userAdd(SysUser user, @RequestParam("role") String role) {
        user.setPassword(BCryptUtil.encode(Constant.initialPassword));
        Set<SysRole> sysRoles = new HashSet<SysRole>();
        String[] roles = role.split(",");
        try{
            for (String role1 :
                    roles) {
                SysRole role2 = roleService.findByName(role1);
                sysRoles.add(role2);
            }
            user.setSysRoles(sysRoles);
            userService.save(user);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/userDel")
    @ResponseBody
    public String userDel(@RequestParam("username") String username) {
        try {
            String[] usernames = username.split(",");
            for (String name :
                    usernames) {
                SysUser user = userService.findByUsername(name);
                userService.delete(user);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return "error";
        }
    }

    @GetMapping("/userInfo")
    public String userInfo(ModelMap map) {
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
        map.addAttribute("user", currentUserDetails);
        SysRole role = roleService.findById(currentUserDetails.getCurrUserRoleId());
        map.addAttribute("role", role);
        return "user/userInfo";
    }

    @PostMapping("/userModify")
    @ResponseBody
        public String userModify(SysUser user) {
        try {
            SysUser sysUser = userService.findByUsername(user.getUsername());
            sysUser.setEmail(user.getEmail());
            sysUser.setMobile(user.getMobile());
            sysUser.setSex(user.getSex());
            userService.save(sysUser);
            SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
            currentUserDetails.setSex(sysUser.getSex());
            currentUserDetails.setEmail(sysUser.getEmail());
            currentUserDetails.setMobile(sysUser.getMobile());
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    @GetMapping("/toChangePwd")
    public String toChangePwd(ModelMap map) {
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
        map.addAttribute("user", currentUserDetails);
        return "user/changePwd";
    }
    @PostMapping("/checkOldPwd")
    @ResponseBody
    public String checkOldPwd(@RequestParam("oldPassword") String oldPassword){
        SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
        boolean match = BCryptUtil.match(oldPassword, currentUserDetails.getPassword());
        return String.valueOf(match);
    }

    @PostMapping("/changePwd")
    @ResponseBody
    public String changePwd(@RequestParam("newPwd") String newPwd,@RequestParam("username") String username) {
        try {
            SysUser user = userService.findByUsername(username);
            String pwd = BCryptUtil.encode(newPwd);
            user.setPassword(pwd);
            userService.save(user);
            SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
            currentUserDetails.setPassword(pwd);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public String changeStatus(String username,Integer status) {
        try{
            SysUser user = userService.findByUsername(username);
            if (user.getStatus().intValue()==status) {
                user.setStatus(user.getStatus().intValue()==1?0:1);
            }
            userService.save(user);
            SecurityUser currentUserDetails = SpringSecurityUtils.getCurrentUserDetails();
            currentUserDetails.setStatus(user.getStatus());
            return "success";
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
