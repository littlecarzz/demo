package com.example.demo.account.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 描述： userDetails的实现类
 * @author littlecar
 * @date 2019/9/5 15:51
 */
public class SecurityUser extends SysUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUser(SysUser sysUser) {
        if (null != sysUser) {
            this.setId(sysUser.getId());
            this.setUsername(sysUser.getUsername());
            this.setPassword(sysUser.getPassword());
            this.setSysRoles(sysUser.getSysRoles());
            this.setMobile(sysUser.getMobile());
            this.setSex(sysUser.getSex());
            this.setStatus(sysUser.getStatus());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new LinkedHashSet<>();
        Set<SysRole> sysRoles = this.getSysRoles();
        if (null != sysRoles) {
            for (SysRole role :
                    sysRoles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
