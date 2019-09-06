package com.example.demo.account.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;
import java.io.Serializable;

/**
 * 描述：角色表
 *
 * @author littlecar
 * @date 2019/9/5 10:10
 */
@Entity
@Table(name = "sys_role")
@Data
public class SysRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    /**
     * 角色名
     */
    @Column(length = 50, nullable = false)
    private String name;
/*    *//**
     * 用户对应的角色
     *//*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns ={@JoinColumn(name = "role_id",nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "user_id",nullable = false)})
    private SysUser sysUser;*/

}
