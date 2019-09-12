package com.example.demo.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 描述：登录记录
 * @author littlecar
 * @date 2019/9/12 9:29
 */
@Entity
@Table(name = "dljl")
@Data
public class Dljl implements Serializable {
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
     * 登陆时间
     */
    @Column(name = "dlsj",length = 50,nullable = false)
    private String dlsj;

    /**
     * 登陆时间
     */
    @Column(name = "dlip",length = 50,nullable = false)
    private String dlip;

    public Dljl() {
    }

    public Dljl(String username, String dlsj, String dlip) {
        this.username = username;
        this.dlsj = dlsj;
        this.dlip = dlip;
    }
}
