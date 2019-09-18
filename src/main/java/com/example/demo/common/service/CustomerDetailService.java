package com.example.demo.common.service;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("UserName: " + username + " not found");
        }
        SecurityUser securityUser = new SecurityUser(user);
        Set<SysRole> roleSet = user.getSysRoles();
        if (roleSet.size() == 1) {//单角色
            securityUser.setCurrUserRoleId(roleSet.iterator().next().getId());
            securityUser.setIsTop(0);
        } else if (roleSet.size() > 1) {//多角色
            securityUser.setIsTop(1);
        } else {//无角色
            securityUser.setIsTop(-1);
        }
        return securityUser;
    }
}
