package com.example.demo.account.web;

import com.example.demo.account.entity.Dljl;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.entity.UserInfo;
import com.example.demo.account.service.impl.DljlServiceImpl;
import com.example.demo.account.service.impl.UserServiceImpl;
import com.example.demo.common.utils.BCryptUtil;
import com.example.demo.common.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/toUserList")
    public String toUserList() {
        return "user/userList";
    }

    @RequestMapping("/userList")
    @ResponseBody
    public Map<String,Object> userList() {
        Map<String, Object> map = new HashMap<>();
        String roles = "";
        List<UserInfo> userInfoList = new LinkedList <>();
        List<SysUser> userList = userService.findAll();
        for (SysUser user :
                userList) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setSex(user.getSex());
            userInfo.setEmail(user.getEmail());
            userInfo.setMobil(user.getMobile());
            userInfo.setStatus(user.getStatus());
            userInfo.setLastTime(dljlService.findLastTimeByUsername(user.getUsername()));
            Set<SysRole> sysRoles = user.getSysRoles();
            int i=0;
            for (SysRole role :
                    sysRoles) {
                if(i==sysRoles.size()-1){
                    roles+= Constant.roleMap.get(role.getName());
                }else{
                    roles+=Constant.roleMap.get(role.getName())+";";
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
    public String userAdd(SysUser user, @RequestParam("role") String role) {
        user.setPassword(BCryptUtil.encode("123456"));
        Set<SysRole> sysRoles = new HashSet<SysRole>();

        return null;
    }
}
