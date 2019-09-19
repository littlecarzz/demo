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
    /**
     * 用户角色：0单角色，1多角色，-1没有角色
     */
    private Integer isTop;
    /**
     * 当前用户角色Id
     */
    private Long currUserRoleId;
    /**
     * 用户明密码
     */
    private String userPwd;

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
        System.out.println("SecurityUser_getAuthorities");
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

    public Long getCurrUserRoleId() {
        return currUserRoleId;
    }

    public void setCurrUserRoleId(Long currUserRoleId) {
        this.currUserRoleId = currUserRoleId;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
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
