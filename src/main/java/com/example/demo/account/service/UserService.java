package com.example.demo.account.service;

import com.example.demo.account.entity.SysUser;

import java.util.List;

/**
 * 描述：
 *
 * @author littlecar
 * @date 2019/9/5 10:32
 */
public interface UserService {
    SysUser findByUsername(String username);

    List<SysUser> findAll();
}
