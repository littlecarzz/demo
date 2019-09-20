package com.example.demo.account.web;

import com.example.demo.account.entity.*;
import com.example.demo.account.service.impl.DljlServiceImpl;
import com.example.demo.account.service.impl.RoleServiceImpl;
import com.example.demo.account.service.impl.UserServiceImpl;
import com.example.demo.common.utils.BCryptUtil;
import com.example.demo.common.utils.Constant;
import com.example.demo.common.utils.SpringSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/12 10:11
 */
@Controller
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
        System.out.println(username);
        Map<String, Object> map = new HashMap<>();
        String roles = "";
        List<UserInfo> userInfoList = new LinkedList <>();
        List<SysUser> userList = new LinkedList<>();
        if (username != null && username!="" && username!="undefined") {
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
        user.setPassword(BCryptUtil.encode("123456"));
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
            System.out.println(e.toString());
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
        map.addAttribute("role", Constant.roleMap.get(role.getName()));
        System.out.println(currentUserDetails.getEmail());
        return "user/userInfo";
    }
    @GetMapping("/changePwd")
    public String changePwd() {
        return "user/changePwd";
    }
}
