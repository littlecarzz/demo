package com.example.demo.common.service;

import com.example.demo.account.entity.SecurityUser;
import com.example.demo.account.entity.SysUser;
import com.example.demo.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
        return securityUser;
    }
}
