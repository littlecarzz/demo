package com.example.demo.account.entity;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 描述：用户表
 * @author littlecar
 * @date 2019/9/5 9:55
 */
@Entity
@Table(name = "user")
@Data
public class SysUser implements Serializable {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username",length = 50,nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(name="password",length = 100,nullable = false)
    private String password;

    /**
     * 联系电话
     */
    @Column(length = 50,nullable = false)
    private String mobile;

    /**
     * 性别
     */
    @Column(length = 5,nullable = false)
    private String sex;

    /**
     * 邮箱
     */
    @Column(length = 100,nullable = false)
    private String email;

    /**
     * 用户的角色集合
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns ={@JoinColumn(name = "user_id",nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id",nullable = false)})
    private Set<SysRole> sysRoles = new HashSet<SysRole>();

    /**
     * 状态：1启用，0禁用
     */
    @Column(length = 2,nullable = false)
    private Integer status;

    public SysUser() {
    }

    public SysUser(String username, String password, String mobile, String sex, Set<SysRole> sysRoles,Integer status) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.sex = sex;
        this.sysRoles = sysRoles;
        this.status = status;
    }

}
