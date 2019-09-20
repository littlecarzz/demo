package com.example.demo.common.service;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysRole;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.service.UserService;
import com.example.demo.account.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashSet;
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
        if (username.contains("@@@")) {
            issub=true;
            String[] split = username.split("@@@");
            username = split[0];
            roleId = Long.valueOf(split[1]);
        }
        SysUser user = userService.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("UserName: " + username + " not found");
        }
        if (user.getStatus().intValue() == 0) {
            throw new DisabledException(user.getUsername() + " is disabled");
        }
        SecurityUser securityUser = new SecurityUser(user);
        Set<SysRole> roleSet = user.getSysRoles();
        Collection<GrantedAuthority> authorities = new LinkedHashSet<>();
        if (roleSet.size() == 1 || issub) {//单角色或子角色
            if (issub) {//子角色
                SysRole role = roleService.findById(roleId);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorities.add(authority);
                securityUser.setAuthorities(authorities);
                securityUser.setCurrUserRoleId(role!=null?role.getId():null);
            }else{//单角色
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleSet.iterator().next().getName());
                authorities.add(authority);
                securityUser.setAuthorities(authorities);
                securityUser.setCurrUserRoleId(roleSet.iterator().next().getId());
            }
            securityUser.setIsTop(0);
        } else if (roleSet.size() > 1) {//多角色
            for (SysRole role :
                    roleSet) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorities.add(authority);
            }
            securityUser.setAuthorities(authorities);
            securityUser.setIsTop(1);
        } else {//无角色
            securityUser.setIsTop(-1);
        }
        return securityUser;
    }
}
