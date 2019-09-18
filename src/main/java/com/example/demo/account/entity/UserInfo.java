package com.example.demo.account.entity;

import lombok.Data;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/12 10:42
 */
@Data
public class UserInfo {

    private String username;
    private String sex;
    private String email;
    private String mobile;
    private String roles;
    private Integer status;
    private String lastTime;

    public UserInfo(String username, String sex, String email, String mobile, String roles, Integer status, String lastTime) {
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.mobile = mobile;
        this.roles = roles;
        this.status = status;
        this.lastTime = lastTime;
    }

    public UserInfo() {
    }
}
