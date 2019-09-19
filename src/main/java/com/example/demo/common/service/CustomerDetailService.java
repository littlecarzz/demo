package com.example.demo.common.service;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.service.RoleService;
import com.example.demo.account.service.UserService;
import com.example.demo.account.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 15:30
 */
@Component
public class CustomerDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean issub = false;
        Long roleId = null;
        System.out.println(username);
        if (username.contains("@@@")) {
            issub=true;
            username = username.split("@@@")[0];
            roleId = Long.valueOf(username.split("@@@")[1]);
        }
        SysUser user = userService.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("UserName: " + username + " not found");
        }
        SecurityUser securityUser = new SecurityUser(user);
        Set<SysRole> roleSet = user.getSysRoles();
        if (roleSet.size() == 1 || issub) {//单角色或子角色
            if (issub) {
                SysRole role = roleService.findById(roleId);
                securityUser.setCurrUserRoleId(role!=null?role.getId():null);
            }else{
                securityUser.setCurrUserRoleId(roleSet.iterator().next().getId());
            }
            securityUser.setIsTop(0);
        } else if (roleSet.size() > 1) {//多角色
            securityUser.setIsTop(1);
        } else {//无角色
            securityUser.setIsTop(-1);
        }
        return securityUser;
    }
}
